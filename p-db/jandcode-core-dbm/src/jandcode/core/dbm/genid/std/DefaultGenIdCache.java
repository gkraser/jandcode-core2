package jandcode.core.dbm.genid.std;

import jandcode.commons.error.*;
import jandcode.core.dbm.genid.*;

/**
 * Простой кеш для genid
 */
public class DefaultGenIdCache implements GenIdCache {

    private long currentValue;
    private long maxValue;
    private long step;

    public DefaultGenIdCache(long maxValue, long cacheSize, long step) {
        this.maxValue = maxValue;
        this.step = step;
        this.currentValue = this.maxValue - (cacheSize - 1) * step;
    }

    public boolean hasNextId() {
        return currentValue <= maxValue;
    }

    public long getNextId() {
        if (!hasNextId()) {
            throw new XError("cache empty");
        }
        long v = currentValue;
        currentValue = currentValue + step;
        return v;
    }
}
