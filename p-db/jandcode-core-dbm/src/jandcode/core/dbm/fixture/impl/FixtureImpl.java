package jandcode.core.dbm.fixture.impl;

import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.fixture.*;
import jandcode.core.store.*;

public class FixtureImpl extends Named implements Fixture {

    private Model model;
    private NamedList<FixtureTable> tables = new DefaultNamedList<>();

    public FixtureImpl(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public NamedList<FixtureTable> getTables() {
        return tables;
    }

    public NamedList<Store> getStores() {
        NamedList<Store> res = new DefaultNamedList<>();
        for (var t : tables) {
            res.add(t.getStore());
        }
        return res;
    }

    public FixtureTable table(String name) {
        FixtureTable t = tables.find(name);
        if (t == null) {
            t = new FixtureTableImpl(this, name);
            tables.add(t);
        }
        return t;
    }

}
