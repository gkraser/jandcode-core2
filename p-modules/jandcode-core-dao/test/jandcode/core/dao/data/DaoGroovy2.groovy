package jandcode.core.dao.data

import jandcode.core.dao.*

class DaoGroovy2 extends Dao2 {
    @DaoMethod
    public int sum(int a, int b) {
        return super.sum(a, b);
    }
}