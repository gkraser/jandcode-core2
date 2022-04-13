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
    private FixtureRangeId rangeId;

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
        return store.add(data);
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

    public FixtureRangeId getRangeId() {
        if (rangeId == null) {
            return calcRangeIdByStore();
        }
        return rangeId;
    }
}
