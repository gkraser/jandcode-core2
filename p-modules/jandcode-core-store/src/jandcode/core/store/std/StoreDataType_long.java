package jandcode.core.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.store.*;

public class StoreDataType_long extends BaseStoreDataType {

    public StoreDataType_long() {
        setDataType(VariantDataType.LONG);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        rawRec.setRawValue(index, value);
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return UtCnv.toLong(rawRec.getRawValue(index));
    }

}
