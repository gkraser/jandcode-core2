package jandcode.core.dao.impl;

import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoMethodDefImpl implements DaoMethodDef {

    private Class cls;
    private Method method;

    public DaoMethodDefImpl(Class cls, Method method) {
        this.cls = cls;
        this.method = method;
    }

    public Class getCls() {
        return cls;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return method.getName();
    }

}
