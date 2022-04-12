package jandcode.core.dbm.genid.impl;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dbstruct.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.genid.*;
import org.slf4j.*;

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


}
