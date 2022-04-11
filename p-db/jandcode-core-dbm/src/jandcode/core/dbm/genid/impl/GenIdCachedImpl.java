package jandcode.core.dbm.genid.impl;

import jandcode.commons.error.*;
import jandcode.core.dbm.genid.*;

/**
 * Кешированный генератор
 */
public class GenIdCachedImpl extends GenIdWrapper {

    private long cacheSize;
    private GenIdDriver driver;
    private GenIdCache cache;

    public GenIdCachedImpl(GenIdImpl wrapper, GenIdDriver driver, long cacheSize) {
        super(wrapper);
        if (cacheSize < 1) {
            throw new XError("Размер кеша должен быть > 0");
        }
        this.driver = driver;
        this.cacheSize = cacheSize;
    }

    public long getNextId() {
        if (cache == null || !cache.hasNextId()) {
            try {
                cache = driver.getGenIdCache(wrapper, cacheSize);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return cache.getNextId();
    }

}
