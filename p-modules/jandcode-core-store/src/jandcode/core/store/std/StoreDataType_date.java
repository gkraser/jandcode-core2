package jandcode.core.store.std;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;
import jandcode.core.store.*;

public class StoreDataType_date extends BaseStoreDataType {

    public StoreDataType_date() {
        setDataType(VariantDataType.DATE);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        if (value != null) {
            XDate dt = UtCnv.toDate(value);
            rawRec.setRawValue(index, dt);
        } else {
            rawRec.setRawValue(index, value);
        }
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return UtCnv.toDate(rawRec.getRawValue(index));
    }

}
