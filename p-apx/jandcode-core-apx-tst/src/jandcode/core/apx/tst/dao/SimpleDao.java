package jandcode.core.apx.tst.dao;

import jandcode.core.dao.*;

/**
 * Простое dao для тестирования вызовов jsonrpc
 */
public class SimpleDao extends BaseDao {

    @DaoMethod
    public String noParams() throws Exception {
        return "noParams";
    }

    @DaoMethod
    public String params2(String p1, int p2) throws Exception {
        return "params2: p1=[" + p1 + "], p2=[" + p2 + "]";
    }


}
