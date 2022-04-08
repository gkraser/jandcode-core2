package jandcode.core.dbm.test;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.impl.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.std.*;

/**
 * Враппер для базы данных. При установке соединения может проверить наличие базы
 * и создать ее при необходимости.
 */
public class TestDbWrapper extends ModelDbWrapper {

    private DbmTestSvc dbmTestSvc;

    public TestDbWrapper(Db db, DbmTestSvc dbmTestSvc) {
        super(db, true, false);
        this.dbmTestSvc = dbmTestSvc;
    }

    protected Db getWrapConnected() {

        // проверяем, что база существует и создаем ее при необходимости
        String cfgKey = "test/" + getClass().getName() + "/dbprepare/" + dbmTestSvc.getModelName() + "/prepared";
        if (!dbmTestSvc.getApp().getConf().getBoolean(cfgKey)) {
            dbmTestSvc.getApp().getConf().setValue(cfgKey, true);
            try {
                doPrepare();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }

        return super.getWrapConnected();
    }

    void doPrepare() throws Exception {
        App app = dbmTestSvc.getApp();
        Model model = dbmTestSvc.getModel();
        //
        boolean createDb = app.getConf().getBoolean("test/dbm/create-db");
        if (!createDb) {
            return;
        }
        boolean dbCreated = false;
        DbManagerService man = model.getDbSource().bean(DbManagerService.class);
        if (!man.existDatabase()) {
            dbmTestSvc.utils.delim("create database");
            dbmTestSvc.showDb();
            man.createDatabase();
            dbmTestSvc.utils.delim();
            dbCreated = true;
        }
        //
        boolean createDbStruct = app.getConf().getBoolean("test/dbm/create-db-struct");
        if (!createDbStruct) {
            return;
        }
        Mdb mdb = model.createMdb(true);
        mdb.connect();
        try {
            CliDbTools dbTools = new CliDbTools(app, model);
            String createSql = dbTools.grabCreateSql();
            String crcScript = UtString.md5Str(createSql);
            //
            TestDbProps props = new TestDbProps(mdb);
            String crcInDb = props.getProp("dbstruct.crc");
            if (!crcScript.equals(crcInDb)) {
                if (!dbCreated) {
                    props.reset();
                    mdb.disconnect();
                    mdb.getDbSource().disconnectAll();
                    dbmTestSvc.utils.delim("recreate database");
                    dbmTestSvc.showDb();
                    man.dropDatabase();
                    man.createDatabase();
                    mdb.connect();
                }
                System.out.println("create.sql: start");
                dbmTestSvc.stopwatch.start("create.sql");
                mdb.execScript(createSql);
                dbmTestSvc.stopwatch.stop("create.sql");
                props.setProp("dbstruct.crc", crcScript);
            }
        } finally {
            mdb.disconnect();
        }
    }

}

