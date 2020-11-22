package jandcode.core.db.impl;

import jandcode.core.db.*;

public class DbParamsImpl implements DbParams {

    private int fetchSize = -1;

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

}
