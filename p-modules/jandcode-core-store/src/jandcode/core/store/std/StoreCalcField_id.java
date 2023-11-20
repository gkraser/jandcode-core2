package jandcode.core.store.std;

import jandcode.commons.*;
import jandcode.core.store.*;

/**
 * Вычисляемое id значение для поля.
 * С кешированием, т.е. генерируется один раз для записи.
 * Если есть несколько такиих полей, то значение каждого будет уникальное.
 * <p>
 * id - это просто нарастающая последовательность long
 */
public class StoreCalcField_id extends BaseStoreCalcField {

    public StoreCalcField_id() {
        setCached(true);
    }

    protected Object doCalcValue(StoreField field, StoreRecord record) {
        String key = "calcfield.id.COMMON.last";
        long lastId = UtCnv.toLong(record.getStore().getCustomProp(key));
        lastId++;
        record.getStore().setCustomProp(key, lastId);
        return lastId;
    }

}
