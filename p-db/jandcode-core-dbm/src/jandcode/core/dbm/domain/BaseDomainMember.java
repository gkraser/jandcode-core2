package jandcode.core.dbm.domain;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

public abstract class BaseDomainMember extends BaseModelMember implements IDomainMember {

    private Domain domain;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        onConfigureMember();
    }


    /**
     * Конфигурирование объекта по данным домена
     */
    protected abstract void onConfigureMember() throws Exception;

    /**
     * Инициализация свойств объекта по данным getDomain().getConf()
     */
    protected void applyRtAttrs() {
        UtReflect.getUtils().setProps(this, getDomain().getConf());
    }

    public Domain getDomain() {
        if (domain == null) {
            throw new XError("Экземпляр компонента создан вне контекста домена: {0}", getClass());
        }
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Model getModel() {
        return getDomain().getModel();
    }

    public App getApp() {
        return getDomain().getApp();
    }

}
