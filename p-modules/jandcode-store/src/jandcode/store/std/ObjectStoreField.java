package jandcode.store.std;

import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class ObjectStoreField extends BaseStoreField {

    public ObjectStoreField() {
        setDataType(VariantDataType.OBJECT);
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        rawRec.setRawValue(getIndex(), value);
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return rawRec.getRawValue(getIndex());
    }

}
