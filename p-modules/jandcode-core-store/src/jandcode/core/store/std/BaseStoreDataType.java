package jandcode.core.store.std;

import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.store.*;

/**
 * Предок для StoreDataType
 */
public abstract class BaseStoreDataType extends BaseComp implements StoreDataType {

    private VariantDataType dataType = VariantDataType.OBJECT;

    public VariantDataType getDataType() {
        return dataType;
    }

    public void setDataType(VariantDataType dataType) {
        this.dataType = dataType;
    }

    public boolean isFieldValueNull(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return rawRec.getRawValue(index) == null;
    }
    
}
