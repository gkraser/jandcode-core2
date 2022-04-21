package jandcode.core.dao.data;

import jandcode.core.dao.*;

public abstract class AbstractDao1_ok extends BaseDao implements IDaoIntf1 {

    public IDaoIntf1 impl() {
        return new AbstractDao1_impl();
    }

    public String m2() {
        return "m2 from AbstractDao1_ok";
    }

}
