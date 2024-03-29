package jandcode.commons.reflect.impl;

import jandcode.commons.error.*;
import jandcode.commons.reflect.*;

import java.lang.reflect.*;

public class ReflectRecordFieldImpl extends BaseReflectProps implements ReflectRecordField {

    private String name;
    private Class type;
    private Method getter;
    private Method setter;
    private Field field;
    private int weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getValue(Object inst) {
        if (this.getter == null) {
            return null;
        }
        try {
            return this.getter.invoke(inst);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public void setValue(Object inst, Object value) {
        if (this.setter == null) {
            return;
        }
        try {
            this.setter.invoke(inst, value);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    //////

    void setPropIfNotExist(String propName, Object propValue) {
        if (propValue == null) {
            return;
        }
        if (this.props == null || !this.props.containsKey(propName)) {
            setProp(propName, propValue);
        }
    }

    //////

    /**
     * Вес поля. Чем больше, тем ниже по списку.
     * Определяется порядком декларации полей в классе.
     */
    int getWeight() {
        return weight;
    }

    void setWeight(int weight) {
        this.weight = weight;
    }

}
