package jandcode.core.dao.data;

import jandcode.core.*;
import jandcode.core.dao.*;

import static org.junit.jupiter.api.Assertions.*;

public class Filter1 extends BaseComp implements DaoFilter {

    public void execDaoFilter(DaoFilterType type, DaoContext ctx) throws Exception {
        if (type == DaoFilterType.before) {
            assertNotNull(ctx.getDaoInvoker());
            assertNotNull(getApp());
        }
        System.out.println("filter: " + type + ": " + getName());
    }

}
