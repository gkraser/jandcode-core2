package jandcode.core.apx.data

import jandcode.core.dao.*

class Dao1 extends BaseDao {

    @DaoMethod
    String str1(String p1) {
        return "str1:" + p1
    }

}
