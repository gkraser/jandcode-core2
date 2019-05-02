package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;
import jandcode.store.*;

public class StoreDataType_datetime extends BaseStoreDataType {

    public StoreDataType_datetime() {
        setDataType(VariantDataType.DATETIME);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        if (value != null) {
            XDateTime dt = UtCnv.toDateTime(value);
            rawRec.setRawValue(index, dt);
        } else {
            rawRec.setRawValue(index, value);
        }
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return UtCnv.toDateTime(rawRec.getRawValue(index));
    }

}
