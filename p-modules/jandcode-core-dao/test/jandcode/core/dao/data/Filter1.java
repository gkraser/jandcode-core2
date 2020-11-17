package jandcode.core.dao.data;

import jandcode.core.dao.*;

import static org.junit.jupiter.api.Assertions.*;

public class Filter1 extends BaseDaoFilter {

    public void execDaoFilter(DaoFilterType type, DaoContext ctx) throws Exception {
        if (type == DaoFilterType.before) {
            assertNotNull(getDaoInvoker());
            assertNotNull(getApp());
        }
        System.out.println("filter: " + type + ": " + getName());
    }

}
