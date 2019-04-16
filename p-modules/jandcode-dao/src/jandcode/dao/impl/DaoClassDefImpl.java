package jandcode.dao.impl;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.dao.*;

import java.lang.reflect.*;

public class DaoClassDefImpl implements DaoClassDef {

    private static IgnoredNameDaoMethods ignoredNameDaoMethods = new IgnoredNameDaoMethods();

    private Class cls;
    private NamedList<DaoMethodDef> methods = new DefaultNamedList<>();

    public DaoClassDefImpl(Class cls) {
        this.cls = cls;
        grabMethods(this.methods);
    }

    public Class getCls() {
        return cls;
    }

    public NamedList<DaoMethodDef> getMethods() {
        return methods;
    }

    public String getName() {
        return cls.getName();
    }

    protected void grabMethods(NamedList<DaoMethodDef> methods) {

        for (Method mt : this.cls.getMethods()) {
            int md = mt.getModifiers();
            if (!Modifier.isPublic(md)) {
                continue; // не публичный
            }
            if (isIgnored(mt)) {
                continue;
            }
            String nm = mt.getName();
            if (methods.find(nm) != null) {
                throw new XError("В dao-классе {0} дублирование имени публичного метода {1}", this.cls.getName(), mt.getName());
            }
            DaoMethodDef mdef = new DaoMethodDefImpl(this.cls, mt);
            methods.add(mdef);
        }

    }

    protected boolean isIgnored(Method mt) {
        return ignoredNameDaoMethods.isIgnore(mt.getName());
    }

}
