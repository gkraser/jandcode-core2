package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class DateStoreField extends BaseStoreField {

    public DateStoreField() {
        setDataType(VariantDataType.DATETIME);
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        if (value != null) {
            XDateTime dt = UtCnv.toDateTime(value);
            dt = dt.clearTime();
            rawRec.setRawValue(getIndex(), dt);
        } else {
            rawRec.setRawValue(getIndex(), value);
        }
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return UtCnv.toDateTime(rawRec.getRawValue(getIndex()));
    }

}
