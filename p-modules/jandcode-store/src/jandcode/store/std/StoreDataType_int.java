package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.store.*;

public class StoreDataType_int extends BaseStoreDataType {

    public StoreDataType_int() {
        setDataType(VariantDataType.INT);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        rawRec.setRawValue(index, value);
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return UtCnv.toInt(rawRec.getRawValue(index));
    }

}
