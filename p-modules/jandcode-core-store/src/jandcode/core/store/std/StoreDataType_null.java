package jandcode.core.store.std;

import jandcode.commons.variant.*;
import jandcode.core.store.*;

public class StoreDataType_null extends BaseStoreDataType {

    public StoreDataType_null() {
        setDataType(VariantDataType.OBJECT);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        rawRec.setRawValue(index, null);
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return null;
    }

}
