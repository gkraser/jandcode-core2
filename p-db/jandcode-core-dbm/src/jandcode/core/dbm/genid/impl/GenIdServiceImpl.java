package jandcode.core.dbm.genid.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dbstruct.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.mdb.*;
import org.slf4j.*;

import java.util.*;

public class GenIdServiceImpl extends BaseModelMember implements GenIdService {

    protected static Logger log = LoggerFactory.getLogger(GenIdService.class);

    private NamedList<GenId> genIds = new DefaultNamedList<>("Не найден genid [{0}]");
    private NamedList<GenIdDriver> drivers = new DefaultNamedList<>();
    private String defaultDriverName;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // драйвер по умолчанию
        this.defaultDriverName = cfg.getConf().getString("driver", "dummy");

        // собираем генераторы из доменов tag.db=true
        DomainDbUtils dbu = new DomainDbUtils(getModel());
        for (Domain d : dbu.getDomains()) {
            // домен в базе данных, создаем генератор
            DomainDb dd = d.bean(DomainDb.class);
            GenId g = new GenIdImpl(getDriver(), d.getName(), dd.getGenIdStart(), dd.getGenIdStep());
            genIds.add(g);
        }

        // собираем генераторы из тегов genid
        for (Conf x : getModel().getConf().getConfs("genid")) {
            GenId g = new GenIdImpl(getDriver(x.getString("driver", this.defaultDriverName)),
                    x.getName(),
                    x.getLong("start", 1000),
                    x.getLong("step", 1)
            );
            genIds.add(g);
        }

    }

    /**
     * Получиь драйвер по имени. Если его нет в списке, то он создается
     */
    protected GenIdDriver getDriver(String name) {
        GenIdDriver drv = this.drivers.find(name);
        if (drv == null) {
            try {
                Conf drvConf = getModel().getConf().getConf("genid-driver/" + name);
                drv = (GenIdDriver) getModel().create(drvConf);
                this.drivers.add(drv);
            } catch (Exception e) {
                throw new XErrorMark(e, "создание genid: " + name);
            }
        }
        return drv;
    }

    public GenIdDriver getDriver() {
        return getDriver(this.defaultDriverName);
    }

    //////

    public NamedList<GenIdDriver> getDrivers() {
        return drivers;
    }

    public NamedList<GenId> getGenIds() {
        return genIds;
    }

    public GenId getGenId(String genIdName) {
        return getGenIds().get(genIdName);
    }

    public GenId getGenId(String genIdName, long cacheSize) {
        if (cacheSize < 1) {
            throw new XError("Размер кеша должен быть > 0");
        }
        //
        GenId genId = getGenId(genIdName);
        GenIdDriver drv = genId.getDriver();
        //
        if (cacheSize > 1 && drv.isSupportGenIdCache(genId)) {
            return new GenIdCachedImpl((GenIdImpl) genId, cacheSize);
        } else {
            return genId;
        }
    }

    public void updateCurrentId(String genIdName, long value) {
        GenId genId = getGenId(genIdName);
        GenIdDriver drv = genId.getDriver();
        try {
            if (drv.isSupportUpdateCurrentId(genId)) {
                drv.updateCurrentId(genId, value);
            }
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public void recoverGenIds() throws Exception {
        recoverGenIds(null, false);
    }

    public void recoverGenIds(List<String> genIdNames, boolean throwError) throws Exception {
        if (genIdNames == null) {
            genIdNames = new ArrayList<>();
            for (GenId g : getGenIds()) {
                genIdNames.add(g.getName());
            }
        }

        //
        if (genIdNames.size() == 0) {
            return;
        }

        log.info("start recoverGenId");

        StringBuilder errors = new StringBuilder();

        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {

            // физические таблицы в базе данных
            log.info("grab database struct");
            NamedList<DbMetadataTable> tbls = mdb.getDbSource().bean(DbMetadataService.class).loadTables();

            for (String nm : genIdNames) {
                GenId g = getGenIds().find(nm);
                if (g == null) {
                    continue; // нет генератора
                }
                if (!getDriver().isSupportUpdateCurrentId(g)) {
                    continue; // не поддерживает обновление значения
                }
                DbMetadataTable tb = tbls.find(g.getName());
                if (tb == null) {
                    continue; // нет физической таблицы
                }
                DbMetadataField idf = tb.getFields().find("id");
                if (idf == null) {
                    continue; // нет поля id
                }
                if (!VariantDataType.isNumber(idf.getDbDataType().getDataType())) {
                    continue; // не число
                }

                try {
                    // можно...

                    // берем текущее максимальное
                    long tbMax;
                    DbQuery q = mdb.openQuery("select max(id) from " + tb.getName());
                    try {
                        tbMax = q.getLong(0);
                    } finally {
                        q.close();
                    }

                    long genCur = g.getCurrentId();

                    if (tbMax > genCur) {
                        // обновляем на следующее
                        log.info("update genid {} for cur={}, max={}", g.getName(), genCur, tbMax);
                        updateCurrentId(g.getName(), tbMax + g.getStep());
                    }

                } catch (Exception e) {
                    log.warn("Error for genid " + g.getName(), e);
                    errors.append(UtError.createErrorInfo(e).getText());
                    errors.append("\n");
                }
            }

        } finally {
            mdb.disconnect();
        }

        log.info("stop recoverGenId");

        if (throwError && errors.length() > 0) {
            throw new XError(errors.toString());
        }

    }

}
