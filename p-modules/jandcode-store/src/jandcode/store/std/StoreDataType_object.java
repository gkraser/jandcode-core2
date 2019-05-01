package jandcode.store.std;

import jandcode.commons.variant.*;
import jandcode.store.*;

public class StoreDataType_object extends BaseStoreDataType {

    public StoreDataType_object() {
        setDataType(VariantDataType.OBJECT);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        rawRec.setRawValue(index, value);
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return rawRec.getRawValue(index);
    }

}
