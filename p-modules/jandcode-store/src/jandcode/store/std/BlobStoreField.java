package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.store.*;
import jandcode.store.impl.*;

public class BlobStoreField extends BaseStoreField {

    public BlobStoreField() {
        setDataType(VariantDataType.BLOB);
    }

    public void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value) {
        rawRec.setRawValue(getIndex(), value);
    }

    public Object getFieldValue(IRawRecord rawRec, StoreRecord rec) {
        return UtCnv.toByteArray(rawRec.getRawValue(getIndex()));
    }

}
