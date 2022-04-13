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
}
