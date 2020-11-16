package jandcode.core.dbm.domain;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

public abstract class BaseFieldMember extends BaseModelMember implements IFieldMember {

    private Field field;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        onConfigureMember();
    }


    /**
     * Конфигурирование объекта по данным поля
     */
    protected abstract void onConfigureMember() throws Exception;


    /**
     * Инициализация свойств объекта по данным getField().getConf()
     */
    protected void applyRtAttrs() {
        UtReflect.getUtils().setProps(this, getField().getConf());
    }


    public Field getField() {
        if (field == null) {
            throw new XError("Экземпляр компонента создан вне контекста поля домена: {0}", getClass());
        }
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Model getModel() {
        return getField().getModel();
    }

    public App getApp() {
        return getField().getApp();
    }

}
