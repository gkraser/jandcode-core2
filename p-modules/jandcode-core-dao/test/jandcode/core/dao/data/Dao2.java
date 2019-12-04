package jandcode.core.dao.data;

import jandcode.core.dao.*;

public class Dao2 extends Dao1 {

    @DaoMethod
    public int sum2(int a, int b) {
        return (a + b) * 2;
    }


}
