package jandcode.core.dbm.genid.std;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.genid.*;

/**
 * Базовый класс для драйверов genid
 */
public abstract class BaseGenIdDriver extends BaseModelMember implements GenIdDriver {

    private Conf conf;
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
     * Зарегистрированные genid, которые принадлежат этому драйверу
     */
    protected NamedList<GenId> getGenIds() {
        NamedList<GenId> res = new DefaultNamedList<>();
        GenIdService svc = getModel().bean(GenIdService.class);
        for (GenId genId : svc.getGenIds()) {
            if (genId.getDriver() == this) {
                res.add(genId);
            }
        }
        res.sort();
        return res;
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
