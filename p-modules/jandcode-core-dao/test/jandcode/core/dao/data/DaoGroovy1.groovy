package jandcode.core.dao.data

import jandcode.core.dao.*

class DaoGroovy1 extends BaseDao {

    void notDaoMethod() {
        println "CALLED notDaoMethod"
    }

    @DaoMethod
    void dummy1() {
    }

}
