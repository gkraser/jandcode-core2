package jandcode.core.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.store.*;

public class StoreDataType_double extends BaseStoreDataType {

    public StoreDataType_double() {
        setDataType(VariantDataType.DOUBLE);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            value = null;
        }
        rawRec.setRawValue(index, value);
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        double v = UtCnv.toDouble(rawRec.getRawValue(index));
        if (field.getScale() != StoreField.NO_SCALE) {
            v = UtCnv.round(v, field.getScale());
        }
        if (Double.isNaN(v) || Double.isInfinite(v)) {
            return 0.0;
        }
        return v;
    }

    public boolean isFieldValueNull(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        Object v = rawRec.getRawValue(index);
        if (v == null) {
            return true;
        }
        if (v instanceof Double vd) {
            if (Double.isNaN(vd) || Double.isInfinite(vd)) {
                return true;
            }
        }
        return false;
    }
    
}
