package jandcode.core.dbm.dao.data;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;

public class CheckModelLink extends BaseModelDao {

    @DaoMethod
    public String m1() throws Exception {
        return "m1-ok-" + getModel().getName();
    }

}
