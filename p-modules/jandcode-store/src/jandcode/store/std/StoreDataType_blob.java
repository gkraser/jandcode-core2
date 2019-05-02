package jandcode.store.std;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.store.*;

public class StoreDataType_blob extends BaseStoreDataType {

    public StoreDataType_blob() {
        setDataType(VariantDataType.BLOB);
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        rawRec.setRawValue(index, value);
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return UtCnv.toByteArray(rawRec.getRawValue(index));
    }

}
