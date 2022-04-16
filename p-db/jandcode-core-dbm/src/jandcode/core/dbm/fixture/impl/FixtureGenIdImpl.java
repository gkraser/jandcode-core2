package jandcode.core.dbm.fixture.impl;

import jandcode.commons.error.*;
import jandcode.core.dbm.fixture.*;

public class FixtureGenIdImpl implements IFixtureGenId {

    private long lastId = -1;
    private long startId = -1;
    private long endId = -1;

    public long getNextId() {
        if (this.startId < 0) {
            throw new XError("Не установлена startId, выполните метод rangeId");
        }
        if (this.lastId < 0) {
            this.lastId = this.startId;
            return this.lastId;
        }
        this.lastId++;
        return this.lastId;
    }

    public long getLastId() {
        if (this.lastId < 0) {
            throw new XError("Не выполнялся метод nextId");
        }
        return this.lastId;
    }

    public long getStartId() {
        return this.startId;
    }

    public long getEndId() {
        return this.endId;
    }

    public long skipId(long count) {
        long res = getNextId();
        this.lastId = this.lastId + count;
        return res;
    }

    public void rangeId(long startId, long endId) {
        if (startId >= 0 && endId >= 0) {
            if (endId < startId) {
                throw new XError("startId должен быть меньше endId");
            }
        }
        this.startId = startId;
        this.endId = endId;
        this.lastId = -1;
    }

}
