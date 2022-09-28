package jandcode.core.dbm.fixture.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.fixture.*;

import java.util.*;

public class FixtureServiceImpl extends BaseComp implements FixtureService {

    private NamedList<FixtureSuiteDef> fixtureSuites = new DefaultNamedList<>("fixture-suite [{0}] не найден");

    class FixtureSuiteDef extends Named {
        Conf conf;

        public FixtureSuiteDef(Conf conf) {
            setName(conf.getName());
            this.conf = conf;
        }

        public FixtureSuite createInst() {
            return getApp().create(conf, FixtureSuiteImpl.class);
        }

    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // формируем раскрытую conf
        Conf xExp = Conf.create();
        xExp.setValue("fixture-suite", getApp().getConf().getConf("dbm/fixture-suite"));

        ConfExpander exp = UtConf.createExpander(xExp);

        //
        Conf confFs = exp.expand("fixture-suite");
        for (Conf x : confFs.getConfs()) {
            FixtureSuiteDef di = new FixtureSuiteDef(x);
            fixtureSuites.add(di);
        }

    }

    public List<String> getFixtureSuiteNames() {
        return fixtureSuites.getNames();
    }

    public FixtureSuite createFixtureSuite(String name) {
        return fixtureSuites.get(name).createInst();
    }

    public FixtureSuite createFixtureSuite(Conf conf) {
        return new FixtureSuiteDef(conf).createInst();
    }

}
