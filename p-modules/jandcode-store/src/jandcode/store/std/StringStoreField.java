package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class StringStoreField extends BaseStoreField {

    private boolean trimSpace;
    private boolean trimSize;

    public StringStoreField() {
        this(true, true);
    }

    public StringStoreField(boolean trimSpace, boolean trimSize) {
        setDataType(VariantDataType.STRING);
        this.trimSpace = trimSpace;
        this.trimSize = trimSize;
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        if (value != null) {
            String s = UtCnv.toString(value);
            if (trimSpace) {
                s = s.trim();
            }
            int sz = getSize();
            if (sz > 0 && trimSize) {
                if (s.length() > sz) {
                    s = s.substring(0, sz);
                }
            }
            rawRec.setRawValue(getIndex(), s);
        } else {
            rawRec.setRawValue(getIndex(), value);
        }
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return UtCnv.toString(rawRec.getRawValue(getIndex()));
    }

}
