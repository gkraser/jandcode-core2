package jandcode.core.dbm.dbstruct.impl;

import jandcode.commons.named.*;
import jandcode.core.dbm.dbstruct.*;

public class DomainDbIndexFieldImpl extends Named implements DomainDbIndexField {

    private boolean desc;

    public DomainDbIndexFieldImpl(String name, boolean desc) {
        this.setName(name);
        this.desc = desc;
    }

    public boolean isDesc() {
        return desc;
    }

}
