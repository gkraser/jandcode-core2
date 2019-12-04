package jandcode.core.dao.data;

import jandcode.core.dao.*;

public class Filter1 extends BaseDaoFilter {

    public void beforeInvoke(DaoFilterParams p) {
        System.out.println("beforeInvoke: " + getName());
    }

    public void afterInvoke(DaoFilterParams p) {
        System.out.println("afterInvoke: " + getName());
    }

    public void errorInvoke(DaoFilterParams p) {
        System.out.println("errorInvoke: " + getName());
    }
}
