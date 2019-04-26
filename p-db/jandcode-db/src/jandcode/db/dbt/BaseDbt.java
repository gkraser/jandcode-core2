package jandcode.db.dbt;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.db.*;

public abstract class BaseDbt extends Named implements DbDataType {

    private VariantDataType datatype = VariantDataType.OBJECT;

    public VariantDataType getDataType() {
        return datatype;
    }

    public void setDataType(VariantDataType datatype) {
        this.datatype = datatype;
    }

}
