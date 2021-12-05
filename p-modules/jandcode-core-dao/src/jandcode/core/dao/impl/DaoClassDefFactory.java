package jandcode.core.dao.impl;

import jandcode.commons.error.*;
import jandcode.core.dao.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Фабрика dao-классов
 */
public class DaoClassDefFactory {

    protected Map<Class, DaoClassDef> daoClasses = new ConcurrentHashMap<>();

    public DaoClassDef getDaoClassDef(Class cls) {
        if (!Dao.class.isAssignableFrom(cls)) {
            throw new XError("Класс {0} не реализует интерфейс {1} и не может быть использован в качестве dao",
                    cls.getName(), Dao.class.getName());
        }
        DaoClassDef res = daoClasses.get(cls);
        if (res == null) {
            res = new DaoClassDefImpl(cls);
            daoClasses.put(cls, res);
        }
        return res;
    }

}
