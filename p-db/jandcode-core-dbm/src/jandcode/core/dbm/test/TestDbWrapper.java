package jandcode.core.dbm.test;

import jandcode.commons.error.*;
import jandcode.core.db.*;
import jandcode.core.dbm.impl.*;

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
        boolean createDb = dbmTestSvc.getApp().getConf().getBoolean("test/dbm/create-db");
        if (!createDb) {
            return;
        }
        DbManagerService man = dbmTestSvc.getModel().getDbSource().bean(DbManagerService.class);
        if (!man.existDatabase()) {
            dbmTestSvc.utils.delim("create database");
            dbmTestSvc.utils.outMap(man.getDbSource().getProps());
            man.createDatabase();
            dbmTestSvc.utils.delim();
        }
    }

}

