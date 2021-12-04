package jandcode.core.dao.data;

import jandcode.core.*;
import jandcode.core.dao.*;

import static org.junit.jupiter.api.Assertions.*;

public class Dao1 extends BaseDao {

    public void setApp(App app) {
        super.setApp(app);
        System.out.println("setApp called");
    }

    public int sum(int a, int b) {
        assertNotNull(getApp());
        return a + b;
    }

    public boolean isDao2() {
        return this instanceof Dao2;
    }

}
