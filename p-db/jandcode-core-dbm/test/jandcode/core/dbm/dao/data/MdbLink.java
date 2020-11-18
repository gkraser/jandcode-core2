package jandcode.core.dbm.dao.data;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.store.*;

public class MdbLink extends BaseModelDao {

    @DaoMethod
    public String m1() throws Exception {
        Store st = getMdb().loadQuery("select 111");
        String s = st.get(0).getString(0);
        return "m1-ok-mdb-" + getModel().getName() + "-" + s;
    }

}
