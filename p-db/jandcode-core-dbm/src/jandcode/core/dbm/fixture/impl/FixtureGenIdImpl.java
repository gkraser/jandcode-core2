package jandcode.core.dbm.fixture.impl;

import jandcode.core.dbm.fixture.*;

public class FixtureGenIdImpl implements FixtureGenId {

    private long curId = 0;
    private long startId = 0;
    private long endId;

    public long getNextId() {
        this.curId++;
        return this.curId;
    }

    public long getCurId() {
        return this.curId;
    }

    public long getStartId() {
        return this.startId;
    }

    public long getEndId() {
        return this.endId;
    }

    public long skipId(long count) {
        long res = getNextId();
        this.curId = this.curId + count;
        return res;
    }

    public void rangeId(long startId, long endId) {
        this.startId = startId;
        this.endId = endId;
    }

}
