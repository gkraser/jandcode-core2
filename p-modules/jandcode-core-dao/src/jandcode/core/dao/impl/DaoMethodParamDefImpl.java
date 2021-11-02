package jandcode.core.dao.impl;

import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoMethodParamDefImpl implements DaoMethodParamDef {

    private String name;
    private Parameter parameter;
    private Class<?> type;
    private int index;

    public DaoMethodParamDefImpl(String name, Parameter parameter, int index) {
        this.name = name;
        this.parameter = parameter;
        this.type = parameter.getType();
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Class<?> getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
