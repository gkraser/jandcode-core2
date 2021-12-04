package jandcode.core.dbm.dao.data;

import jandcode.core.dbm.dao.*;

public class CheckModelLink extends BaseModelDao {

    public String m1() throws Exception {
        return "m1-ok-" + getModel().getName();
    }

}
