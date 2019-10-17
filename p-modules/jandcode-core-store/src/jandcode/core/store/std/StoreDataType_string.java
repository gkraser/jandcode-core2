package jandcode.core.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.store.*;

public class StoreDataType_string extends BaseStoreDataType {

    private boolean trimSpace;
    private boolean trimSize;

    public StoreDataType_string() {
        this(true, true);
    }

    public StoreDataType_string(boolean trimSpace, boolean trimSize) {
        setDataType(VariantDataType.STRING);
        this.trimSpace = trimSpace;
        this.trimSize = trimSize;
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        if (value != null) {
            String s = UtCnv.toString(value);
            if (trimSpace) {
                s = s.trim();
            }
            int sz = field.getSize();
            if (sz > 0 && trimSize) {
                if (s.length() > sz) {
                    s = s.substring(0, sz);
                }
            }
            rawRec.setRawValue(index, s);
        } else {
            rawRec.setRawValue(index, value);
        }
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return UtCnv.toString(rawRec.getRawValue(index));
    }

}
