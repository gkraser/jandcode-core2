package jandcode.core.dao.impl;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoClassDefImpl implements DaoClassDef {

    private Class cls;
    private NamedList<DaoMethodDef> methods;

    public DaoClassDefImpl(Class cls) {
        this.cls = cls;
        this.methods = new DefaultNamedList<>();
        this.methods.setNotFoundMessage("Не найден dao-метод {0} в классе {1}", this);
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
            DaoMethod an = mt.getAnnotation(DaoMethod.class);
            if (an == null) {
                continue;
            }
            int md = mt.getModifiers();
            if (!Modifier.isPublic(md)) {
                throw new XError("@DaoMethod указан для не публичного метода: {0}", mt);
            }
            String nm = mt.getName();
            if (methods.find(nm) != null) {
                throw new XError("В dao-классе {0} дублирование имени dao-метода {1}", this.cls.getName(), mt.getName());
            }
            DaoMethodDef mdef = new DaoMethodDefImpl(this.cls, mt);
            methods.add(mdef);
        }

    }

}
