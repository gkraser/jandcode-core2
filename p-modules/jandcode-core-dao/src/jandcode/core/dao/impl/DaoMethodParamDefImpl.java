package jandcode.core.dao.impl;

import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoMethodParamDefImpl implements DaoMethodParamDef {

    private String name;
    private Method method;
    private Class<?> type;
    private int index;

    public DaoMethodParamDefImpl(String name, Method method, Class<?> type, int index) {
        this.name = name;
        this.method = method;
        this.type = type;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
