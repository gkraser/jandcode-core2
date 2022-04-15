package jandcode.core.dbm.fixture.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.fixture.*;

import java.util.*;

public class FixtureServiceImpl extends BaseComp implements FixtureService {

    public List<String> getFixtureSuiteNames() {
        List<String> res = new ArrayList<>();
        for (Conf x : getApp().getConf().getConfs("dbm/fixture-suite")) {
            if (x.hasName()) {
                res.add(x.getName());
            }
        }
        return res;
    }

    public FixtureSuite createFixtureSuite(String name) {
        Conf conf = getApp().getConf().getConf("dbm/fixture-suite/" + name);
        return createFixtureSuite(conf);
    }

    public FixtureSuite createFixtureSuite(Conf conf) {
        return getApp().create(conf, FixtureSuiteImpl.class);
    }

}
