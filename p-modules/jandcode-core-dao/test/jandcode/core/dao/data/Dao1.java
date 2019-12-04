package jandcode.core.dao.data;

import jandcode.core.*;
import jandcode.core.dao.*;

public class Dao1 extends BaseDao {

    public void setApp(App app) {
        super.setApp(app);
        System.out.println("setApp called");
    }

    @DaoMethod
    public int sum(int a, int b) {
        return a + b;
    }

    @DaoMethod
    public boolean isDao2() {
        return this instanceof Dao2;
    }

}
