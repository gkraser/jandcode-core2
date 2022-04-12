package jandcode.core.dbm.genid.impl;

import jandcode.commons.error.*;
import jandcode.core.dbm.genid.*;

/**
 * Кешированный генератор
 */
public class GenIdCachedImpl extends GenIdWrapper {

    private long cacheSize;
    private GenIdCache cache;

    public GenIdCachedImpl(GenIdImpl wrapper, long cacheSize) {
        super(wrapper, null);
        if (cacheSize < 1) {
            throw new XError("Размер кеша должен быть > 0");
        }
        this.cacheSize = cacheSize;
    }

    public long getNextId() {
        if (cache == null || !cache.hasNextId()) {
            try {
                cache = getDriver().getGenIdCache(this, cacheSize);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return cache.getNextId();
    }

}
