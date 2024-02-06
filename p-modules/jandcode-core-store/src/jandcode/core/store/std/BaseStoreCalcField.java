package jandcode.core.store.std;

import jandcode.core.*;
import jandcode.core.store.*;

/**
 * Базовый класс для вычислительных полей
 */
public abstract class BaseStoreCalcField extends BaseComp implements StoreCalcField {

    private boolean cached;

    /**
     * Нужно ли кешировать значение. По умолчанию - false.
     */
    public boolean isCached() {
        return cached;
    }

    protected void setCached(boolean cached) {
        this.cached = cached;
    }

    public Object calcValue(StoreField field, StoreRecord record) {
        if (this.cached && record instanceof IRawRecord rawRecord) {
            Object v;
            v = rawRecord.getRawValue(field.getIndex());
            if (v == null) {
                v = doCalcValue(field, record);
                rawRecord.setRawValue(field.getIndex(), v);
            }
            return v;
        } else {
            return doCalcValue(field, record);
        }
    }

    protected abstract Object doCalcValue(StoreField field, StoreRecord record);

}
