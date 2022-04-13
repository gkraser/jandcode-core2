package jandcode.core.dbm.fixture.impl;

import jandcode.core.dbm.fixture.*;

public class FixtureRangeIdImpl implements FixtureRangeId {

    private long startId;
    private long endId;

    public FixtureRangeIdImpl(long startId, long endId) {
        this.startId = startId;
        this.endId = endId;
    }

    public long getStartId() {
        return startId;
    }

    public long getEndId() {
        return endId;
    }

    public String toString() {
        return "FixtureRangeId{" +
                "startId=" + startId +
                ", endId=" + endId +
                '}';
    }

}
