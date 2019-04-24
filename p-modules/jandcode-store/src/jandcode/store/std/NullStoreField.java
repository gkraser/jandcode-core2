package jandcode.store.std;

import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class NullStoreField extends BaseStoreField {

    public NullStoreField() {
        setDataType(VariantDataType.OBJECT);
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        rawRec.setRawValue(getIndex(), null);
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return null;
    }

}
