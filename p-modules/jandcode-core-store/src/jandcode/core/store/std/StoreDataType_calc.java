package jandcode.core.store.std;

import jandcode.commons.variant.*;
import jandcode.core.store.*;

/**
 * Вычисляемое поле
 */
public class StoreDataType_calc extends BaseStoreDataType {

    private StoreDataType baseStoreDataType;
    private StoreCalcField calc;

    public StoreDataType_calc(StoreDataType baseStoreDataType, StoreCalcField calc) {
        this.baseStoreDataType = baseStoreDataType;
        this.calc = calc;
    }

    public StoreDataType getBaseStoreDataType() {
        return baseStoreDataType;
    }

    public VariantDataType getDataType() {
        return baseStoreDataType.getDataType();
    }

    public void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field) {
        // для вычисляемого поля значение нельзя присвоить
    }

    public Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return this.calc.calcValue(field, rec);
    }

    public boolean isFieldValueNull(IRawRecord rawRec, int index, StoreRecord rec, StoreField field) {
        return this.calc.calcValue(field, rec) == null;
    }

}
