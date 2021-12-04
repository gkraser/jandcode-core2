package jandcode.core.dao.impl;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoClassDefImpl implements DaoClassDef {

    private static IgnoreMethods ignoreMethods = new IgnoreMethods();

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

    protected boolean isStopClass(Class cls) {
        return cls == null || cls == BaseDao.class || cls == Object.class;
    }

    protected boolean isIgnore(Method m) {
        return ignoreMethods.isIgnore(m);
    }

    protected void grabMethods(NamedList<DaoMethodDef> methods) {

        validatePackageMethods(this.cls);

        for (Method mt : this.cls.getMethods()) {
            int md = mt.getModifiers();
            if (!Modifier.isPublic(md)) {
                continue;
            }
            if (Modifier.isStatic(md)) {
                continue;
            }
            if (isIgnore(mt)) {
                continue;
            }
            String nm = mt.getName();
            if (methods.find(nm) != null) {
                throw new XError("В dao-классе {0} дублирование имени dao-метода {1}", this.cls.getName(), mt.getName());
            }
            DaoMethodDef mdef = new DaoMethodDefImpl(this.cls, mt);
            methods.add(mdef);
        }

    }

    protected void validatePackageMethods(Class cls) {
        if (isStopClass(cls)) {
            return;
        }
        for (Method mt : cls.getDeclaredMethods()) {
            int md = mt.getModifiers();
            if (md == 0) {
                throw new XError("Метод должен быть public, private или protected: {0}", mt);
            }
        }
        validatePackageMethods(cls.getSuperclass());
    }

}
