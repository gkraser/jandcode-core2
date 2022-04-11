package jandcode.core.dbm.genid.std;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.ddl.impl.*;
import jandcode.core.dbm.genid.*;

import java.util.*;

/**
 * Базовый класс для драйверов genid
 */
public abstract class BaseGenIdDriver extends BaseModelMember implements GenIdDriver, IConfLink, IDDLProvider {

    private Conf conf;
    private List<DDLProvider> ddlProviders;
    private boolean inited;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
    }

    /**
     * conf может содержать ddl
     */
    public Conf getConf() {
        return conf;
    }

    /**
     * Зарегистрированные genid
     */
    protected NamedList<GenId> getGenIds() {
        return getModel().bean(GenIdService.class).getGenIds();
    }

    ////// DDL

    /**
     * ddl провайдеры, которые используются для создания необходимых
     * структур в базе данных.
     */
    protected List<DDLProvider> getDDLProviders() {
        if (ddlProviders == null) {
            synchronized (this) {
                if (ddlProviders == null) {
                    ddlProviders = grabDDLProviders();
                }
            }
        }
        return ddlProviders;
    }

    protected List<DDLProvider> grabDDLProviders() {
        List<DDLProvider> tmp = new ArrayList<>();
        for (Conf x : getConf().getConfs("ddl")) {
            BaseDDLProvider p = getModel().create(x, DefaultDDLProvider.class);
// todo
//  if (!p.isGlobal()) {
//                // если не глобальный, то должен вызыватся только внутри core
//                if (!getModel().isDefinedForDbStruct("bean/" + GenIdService.class.getName())) {
//                    continue;
//                }
//            }
            tmp.add(p);
        }
        return tmp;
    }

    /**
     * Загрузка ddl для создания структур в базе данных.
     */
    public List<DDLOper> loadOpers(DDLStage stage) throws Exception {
        List<DDLOper> res = new ArrayList<>();
        for (DDLProvider p : getDDLProviders()) {
            List<DDLOper> tmp = p.loadOpers(stage);
            if (tmp != null) {
                res.addAll(tmp);
            }
        }
        if (res.size() > 0) {
            return res;
        } else {
            return null;
        }
    }

    ////// interface

    /**
     * Инициализация драйвера.
     * Вызывать в начале каждого метода-реализации интерфейса драйвера.
     */
    protected void initDriver() {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    try {
                        doInitDriver();
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }
                    inited = true;
                }
            }
        }
    }

    ////// implementation

    /**
     * Реализация инициализации драйвера
     */
    protected abstract void doInitDriver() throws Exception;

    public boolean isSupportUpdateCurrentId(GenId genId) {
        return false;
    }

    public void updateCurrentId(GenId genId, long value) throws Exception {
    }

    public boolean isSupportGenIdCache(GenId genId) {
        return false;
    }

    public GenIdCache getGenIdCache(GenId genId, long count) throws Exception {
        throw new XError("unsupported getGenIdCache");
    }

}
