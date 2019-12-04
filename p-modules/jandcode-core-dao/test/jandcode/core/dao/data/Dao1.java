package jandcode.core.dao.data;

import jandcode.core.dao.*;

public class Dao1 extends BaseDao {

    @DaoMethod
    public int sum(int a, int b) {
        return a + b;
    }

}
