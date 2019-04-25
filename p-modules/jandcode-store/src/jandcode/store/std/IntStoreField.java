package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class IntStoreField extends BaseStoreField {

    public IntStoreField() {
        setDataType(VariantDataType.INT);
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        rawRec.setRawValue(getIndex(), value);
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return UtCnv.toInt(rawRec.getRawValue(getIndex()));
    }

}
