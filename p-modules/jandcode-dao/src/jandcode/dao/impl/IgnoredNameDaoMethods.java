package jandcode.dao.impl;

import jandcode.commons.collect.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Игнорируемые имена методов в dao.
 * Все методы, которые присутсвуют в BaseDao и выше, которые groovy добавляет сам,
 * которые имеют '$' в имени.
 */
public class IgnoredNameDaoMethods {

    private Set<String> items = new HashSetNoCase();

    IgnoredNameDaoMethods() {
        DummyGroovyDao d = new DummyGroovyDao();
        for (Method m : d.getClass().getMethods()) {
            items.add(m.getName());
        }
    }

    public Set<String> getItems() {
        return items;
    }

    public boolean isIgnore(String name) {
        if (name.indexOf('$') != -1) {
            return true;
        }
        return items.contains(name);
    }

}
