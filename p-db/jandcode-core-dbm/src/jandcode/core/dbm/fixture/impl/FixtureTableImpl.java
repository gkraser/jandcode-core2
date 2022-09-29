package jandcode.core.dbm.fixture.impl;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.dbdata.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.fixture.*;
import jandcode.core.store.*;

import java.util.*;

public class FixtureTableImpl extends Named implements FixtureTable {

    private Fixture fixture;
    private Store store;
    private FixtureGenIdImpl genId = new FixtureGenIdImpl();

    public FixtureTableImpl(Fixture fixture, String name) {
        this.fixture = fixture;
        setName(name);
        this.store = fixture.getModel().bean(DomainService.class).createStore(name);
        this.store.setName(name);
    }

    public Fixture getFixture() {
        return fixture;
    }

    public Store getStore() {
        return store;
    }

    public StoreRecord add(Map data) {
        StoreRecord rec = store.add(data);
        //
        long id = rec.getLong("id");
        if (id == 0) {
            id = getNextId();
            rec.setValue("id", id);
        }
        //
        return rec;
    }

    public void loadFromFile(String fileName) throws Exception {
        StoreService svcStore = getFixture().getModel().getApp().bean(StoreService.class);
        //
        String ldrName = DbDataUtils.fileNameToStoreLoaderName(fileName);
        if (!svcStore.hasStoreLoader(ldrName)) {
            throw new XError("Для файла {0} не найден StoreLoader: {1}", fileName, ldrName);
        }

        StoreLoader ldr = svcStore.createStoreLoader(ldrName);
        ldr.setStore(getStore());
        ldr.load().fromFileObject(fileName);
    }

    protected FixtureRangeId calcRangeIdByStore() {
        StoreField idField = getStore().findField("id");
        if (idField == null || getStore().size() == 0) {
            return new FixtureRangeIdImpl(0, 0);
        }

        long startId = Long.MAX_VALUE;
        long endId = 0;

        int idIdx = idField.getIndex();
        for (StoreRecord rec : getStore()) {
            long id = rec.getLong(idIdx);
            if (id > endId) {
                endId = id;
            }
            if (id < startId) {
                startId = id;
            }
        }

        return new FixtureRangeIdImpl(startId, endId);
    }

    protected long calcMaxIdInRange(long min, long max) {
        StoreField idField = getStore().findField("id");
        if (idField == null || getStore().size() == 0) {
            return min;
        }

        long res = min;
        if (max < 0) {
            max = Long.MAX_VALUE;
        }

        int idIdx = idField.getIndex();
        for (StoreRecord rec : getStore()) {
            long id = rec.getLong(idIdx);
            if (id > res && id <= max) {
                res = id;
            }
        }

        return res;
    }

    public FixtureRangeId getRangeId() {
        if (getStartId() >= 0 && getEndId() >= 0) {
            return new FixtureRangeIdImpl(getStartId(), getEndId());
        }
        FixtureRangeId rng = calcRangeIdByStore();
        return new FixtureRangeIdImpl(
                getStartId() < 0 ? rng.getStartId() : getStartId(),
                getEndId() < 0 ? rng.getEndId() : getEndId()
        );
    }

    public long getMaxIdInRange() {
        boolean hasRange = getStartId() >= 0;
        if (hasRange) {
            return calcMaxIdInRange(getStartId(), getEndId());
        } else {
            FixtureRangeId rng = calcRangeIdByStore();
            return rng.getEndId();
        }
    }

    ////// genId

    public long getNextId() {
        return genId.getNextId();
    }

    public long getLastId() {
        return genId.getLastId();
    }

    public long getStartId() {
        return genId.getStartId();
    }

    public long getEndId() {
        return genId.getEndId();
    }

    public long skipId(long count) {
        return genId.skipId(count);
    }

    public void rangeId(long startId, long endId) {
        genId.rangeId(startId, endId);
    }
}
