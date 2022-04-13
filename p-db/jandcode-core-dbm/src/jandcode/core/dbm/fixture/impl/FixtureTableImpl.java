package jandcode.core.dbm.fixture.impl;

import jandcode.commons.named.*;
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

}
