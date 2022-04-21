package jandcode.core.dao.impl;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;

public class DaoClassDefImpl implements DaoClassDef {

    private static IgnoreMethods ignoreMethods = new IgnoreMethods();

    private Class cls;
    private Class clsInst;
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

    public Class getClsInst() {
        if (clsInst == null) {
            return cls;
        }
        return clsInst;
    }

    public void setClsInst(Class clsInst) {
        this.clsInst = clsInst;
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
            DaoMethodDef mdef = new DaoMethodDefImpl(this, mt);
            methods.add(mdef);
        }

        // в случае абстрактного класса
        int md = this.cls.getModifiers();
        if (Modifier.isAbstract(md)) {
            // должен быть не абстрактный метод impl
            Method implMethod;
            try {
                implMethod = this.cls.getMethod("impl");
            } catch (NoSuchMethodException e) {
                throw new XError("В абстрактном dao-классе {0} нет метода impl()", this.cls.getName());
            }
            md = implMethod.getModifiers();
            if (Modifier.isAbstract(md)) {
                throw new XError("Метод не должен быть абстрактным: {0}", implMethod);
            }
            if (md == 0) {
                throw new XError("Метод должен быть public: {0}", implMethod);
            }
            // создаем класс - обертку
            Class wrapperCls = ImplClassFactory.getInst().getImplClass(this.cls);
            this.clsInst = wrapperCls;
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
