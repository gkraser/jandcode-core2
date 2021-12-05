package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.core.dao.*;

import java.lang.reflect.*;
import java.util.*;

public class IgnoreMethods {

    private Set<String> ignoreMethods = new HashSet<>();

    public IgnoreMethods() {
        addIgnore(BaseDao.class);
        addIgnore("groovy.lang.GroovyObjectSupport");
    }

    public void addIgnore(Class cls) {
        for (Method m : cls.getMethods()) {
            ignoreMethods.add(m.getName());
        }
    }

    public void addIgnore(String clsName) {
        try {
            Class cls = UtClass.getClass(clsName);
            addIgnore(cls);
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
