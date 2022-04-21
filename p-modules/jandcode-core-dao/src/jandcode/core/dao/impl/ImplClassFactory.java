package jandcode.core.dao.impl;

import jandcode.commons.error.*;
import javassist.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.*;
import java.util.*;

/**
 * Генератор класса, который явлется наследником от абстрактного класса,
 * у которого есть метод impl(), куда и делегируются все вызовы абстрактных
 * методов.
 * <p>
 * Вызывать только один раз для класса!
 */
public class ImplClassFactory {

    private static String SUFFIX = "___IMPL";

    protected Map<Class, Class> cache = new HashMap<>();

    private static ImplClassFactory _inst = new ImplClassFactory();

    public static ImplClassFactory getInst() {
        return _inst;
    }

    private ImplClassFactory() {
    }

    public Class getImplClass(Class cls) {
        Class res = cache.get(cls);
        if (res == null) {
            synchronized (this) {
                res = cache.get(cls);
                if (res == null) {
                    try {
                        res = generateImpl(cls);
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }
                    cache.put(cls, res);
                }
            }
        }
        return res;
    }


    public Class generateImpl(Class origClass) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass super1 = cp.getCtClass(origClass.getName());
        CtClass ctClass = cp.makeClass(origClass.getName() + SUFFIX);
        ctClass.setSuperclass(super1);
        ctClass.setModifiers(java.lang.reflect.Modifier.PUBLIC);

        for (Method m : origClass.getMethods()) {
            int md = m.getModifiers();
            if (!Modifier.isAbstract(md)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("public ");
            String retType = m.getReturnType().getName();
            sb.append(retType);
            sb.append(" ");
            sb.append(m.getName());
            sb.append("(");
            int cnt = 0;
            for (var p : m.getParameters()) {
                cnt++;
                if (cnt > 1) {
                    sb.append(",");
                }
                sb.append(p.getType().getName());
                sb.append(" ");
                sb.append(p.getName());
            }
            sb.append(") {");
            if (!retType.equals("void")) {
                sb.append("return ");
            }
            sb.append("impl().");
            sb.append(m.getName());
            sb.append("(");
            cnt = 0;
            for (var p : m.getParameters()) {
                cnt++;
                if (cnt > 1) {
                    sb.append(",");
                }
                sb.append(p.getName());
            }
            sb.append(");");
            sb.append("}");

            CtMethod newMeth = CtMethod.make(sb.toString(), ctClass);
            ctClass.addMethod(newMeth);

        }

        Class<?> destCls = ctClass.toClass(origClass);

        return destCls;
    }

}
