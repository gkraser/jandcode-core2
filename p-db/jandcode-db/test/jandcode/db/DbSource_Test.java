package jandcode.db;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.test.*;
import jandcode.db.impl.*;
import org.junit.jupiter.api.*;

public class DbSource_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbSourceFactory f = new DbSourceFactory();
        Conf conf = UtConf.create();
        DbSource dbs = f.createDbSource(app, conf);
        utils.outMap(dbs.getConf());
    }

}
