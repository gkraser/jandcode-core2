package jandcode.commons.reflect.impl;

import jandcode.commons.*;
import jandcode.commons.reflect.*;

import java.lang.reflect.*;

public class ReflectTableFieldImpl extends BaseReflectProps implements ReflectTableField {

    private String name;
    private Class type;
    private Method getter;
    private Method setter;
    private Field field;

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

    //////

    void setPropIfNotExist(String propName, String propValue) {
        if (UtString.empty(propValue)) {
            return;
        }
        if (UtString.empty(propName)) {
            return;
        }
        if (this.props == null || !this.props.containsKey(propName)) {
            setProp(propName, propValue);
        }
    }

}
