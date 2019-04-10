package jandcode.commons.str.impl;

import jandcode.commons.str.*;

import java.util.concurrent.*;

/**
 * Базовый предок для дедупликаторов
 */
public abstract class BaseStrDedup implements StrDedup {

    protected ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    public void clear() {
        cache = new ConcurrentHashMap<>();
    }

    public ConcurrentMap<String, String> getCache() {
        return cache;
    }

}
