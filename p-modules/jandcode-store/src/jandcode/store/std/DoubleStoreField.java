package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class DoubleStoreField extends BaseStoreField {

    public DoubleStoreField() {
        setDataType(VariantDataType.DOUBLE);
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        rawRec.setRawValue(getIndex(), value);
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return UtCnv.toDouble(rawRec.getRawValue(getIndex()));
    }

}
