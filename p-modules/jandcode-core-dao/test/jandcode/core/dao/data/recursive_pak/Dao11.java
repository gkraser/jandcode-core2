package jandcode.core.dao.data.recursive_pak;

import jandcode.core.dao.*;

public class Dao11 extends BaseDao {

    public int sum(int a, int b) {
        return a + b;
    }

    public String method1() {
        String mn = "";
        DaoHolderItem dhi = getContext().getDaoHolderItem();
        if (dhi != null) {
            mn = dhi.getName();
        }
        return mn;
    }

}
