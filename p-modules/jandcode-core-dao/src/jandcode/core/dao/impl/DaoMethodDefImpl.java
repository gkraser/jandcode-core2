package jandcode.core.dao.impl;

import jandcode.commons.named.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoMethodDefImpl implements DaoMethodDef {

    private DaoClassDef classDef;
    private Method method;
    private NamedList<DaoMethodParamDef> params = new DefaultNamedList<>();

    public DaoMethodDefImpl(DaoClassDef classDef, Method method) {
        this.classDef = classDef;
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

    public DaoClassDef getClassDef() {
        return classDef;
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
