package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;
import java.util.*;

public class IgnoreMethods {

    private Set<String> ignoreMethods = new HashSet<>();

    public IgnoreMethods() {
        addIgnoreClass(BaseDao.class);
        addIgnoreClass("groovy.lang.GroovyObjectSupport");
        addIgnore("impl");
    }

    public void addIgnore(String methodName) {
        ignoreMethods.add(methodName);
    }

    public void addIgnoreClass(Class cls) {
        for (Method m : cls.getMethods()) {
            addIgnore(m.getName());
        }
    }

    public void addIgnoreClass(String clsName) {
        try {
            Class cls = UtClass.getClass(clsName);
            addIgnoreClass(cls);
        } catch (Exception e) {
            // ignore
        }
    }

    public Set<String> getIgnoreMethods() {
        return ignoreMethods;
    }

    public boolean isIgnore(Method m) {
        String nm = m.getName();
        if (nm.indexOf("$") != -1) {
            return true;
        }
        return ignoreMethods.contains(m.getName());
    }

}
