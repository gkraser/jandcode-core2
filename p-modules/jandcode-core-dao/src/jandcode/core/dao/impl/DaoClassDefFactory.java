package jandcode.core.dao.impl;

import jandcode.core.dao.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Фабрика dao-классов
 */
public class DaoClassDefFactory {

    protected Map<Class, DaoClassDef> daoClasses = new ConcurrentHashMap<>();

    public DaoClassDef getDaoClassDef(Class cls) {
        DaoClassDef res = daoClasses.get(cls);
        if (res == null) {
            res = new DaoClassDefImpl(cls);
            daoClasses.put(cls, res);
        }
        return res;
    }

}
