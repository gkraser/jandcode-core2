package jandcode.core.dbm.dao.data;

import jandcode.core.dbm.dao.*;
import jandcode.core.store.*;

public class DbLink extends BaseModelDao {

    public String m1() throws Exception {
        Store st = getMdb().loadQuery("select 111");
        String s = st.get(0).getString(0);
        return "m1-ok-" + getModel().getName() + "-" + s;
    }

}
