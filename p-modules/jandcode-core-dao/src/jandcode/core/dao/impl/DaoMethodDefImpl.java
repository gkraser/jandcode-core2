package jandcode.core.dao.impl;

import jandcode.commons.named.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoMethodDefImpl implements DaoMethodDef {

    private Class cls;
    private Method method;
    private NamedList<DaoMethodParamDef> params = new DefaultNamedList<>();

    public DaoMethodDefImpl(Class cls, Method method) {
        this.cls = cls;
        this.method = method;
        grabParams();
    }

    protected void grabParams() {
        Parameter[] prms = this.method.getParameters();
        for (int i = 0; i < prms.length; i++) {
            Parameter p = prms[i];
            DaoMethodParamDef mp = new DaoMethodParamDefImpl(p.getName(), p, i);
            this.params.add(mp);
        }
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

    public NamedList<DaoMethodParamDef> getParams() {
        return params;
    }

}
