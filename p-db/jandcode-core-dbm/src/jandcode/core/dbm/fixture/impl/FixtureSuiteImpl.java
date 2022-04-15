package jandcode.core.dbm.fixture.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.fixture.*;

import java.util.*;

public class FixtureSuiteImpl extends BaseComp implements FixtureSuite {

    private Conf conf;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
    }

    public Conf getConf() {
        return conf;
    }

    public List<FixtureBuilder> createBuilders() {
        List<FixtureBuilder> res = new ArrayList<>();
        for (Conf x : getConf().getConfs("fixture-builder")) {
            FixtureBuilder b = (FixtureBuilder) getApp().create(x);
            res.add(b);
        }
        return res;
    }

}
