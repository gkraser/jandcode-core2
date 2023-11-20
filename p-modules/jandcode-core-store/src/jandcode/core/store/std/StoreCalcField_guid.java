package jandcode.core.store.std;

import jandcode.core.store.*;

import java.util.*;

/**
 * Вычисляемое guid значение для поля. С кешированием, т.е. генерируется один раз для записи
 */
public class StoreCalcField_guid extends BaseStoreCalcField {

    public StoreCalcField_guid() {
        setCached(true);
    }

    protected Object doCalcValue(StoreField field, StoreRecord record) {
        return UUID.randomUUID().toString();
    }

}
