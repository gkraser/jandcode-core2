package jandcode.db.test

import jandcode.commons.*
import jandcode.commons.test.*
import jandcode.core.*
import jandcode.core.test.*
import jandcode.db.*

/**
 * Утилиты для поддержки тестирования модуля db и драйверов на его основе.
 * В setUp создается при необходимости база данных, описанная в dbsource test.1
 * и с ней устанавливается соединение, доступное через db.
 */
class DbSimpleTestSvc extends BaseTestSvc {

    String dbsourceName = "test1"

    DbService svc
    DbSource dbs
    Db db

    String checkDbDatatype_table = "checkDbType"

    void setUp() throws Exception {
        super.setUp();
        //
        App app = testSvc(AppTestSvc).app
        svc = app.bean(DbService)
        dbs = svc.getDbSource(dbsourceName)
        db = dbs.createDb(true)
        //
        String cfgKey = "test/dbprepare/${dbsourceName}/prepared"
        if (!app.conf.getBoolean(cfgKey)) {
            doPrepare()
            app.conf.setValue(cfgKey, true)
        }
        //
        db.connect()
    }

    void tearDown() throws Exception {
        super.tearDown()
        if (db != null) {
            db.disconnectForce()
        }
    }

    def doPrepare() {
        DbManagerService man = dbs.bean(DbManagerService)
        if (!man.existDatabase()) {
            man.createDatabase()
        }
    }

    //////

    /**
     * Создание полей всех типов для проверки их имен.
     * Для каждого типа создается своя таблица.
     */
    void checkSqlTypes() {
        println "checkSqlTypes for: ${db.dbSource.dbDriver.name}"
        for (DbDataType dbt : db.dbSource.dbDriver.dbDataTypes) {
            String sqltype = dbt.getSqlType(20)
            if (UtString.empty(sqltype)) {
                println "  ${UtString.padRight(dbt.name, 20)}-> SKIP"
                continue
            }
            String tn = "${checkDbDatatype_table}_sqltype_${dbt.name}"
            String sqlCreate = "create table ${tn} (f1 ${sqltype})"
            println "  ${UtString.padRight(dbt.name, 20)}-> ${UtString.padRight(sqltype, 20)}-> ${sqlCreate}"
            try {
                db.execQuery("drop table ${tn}")
            } catch (e) {
                // ignore
            }
            db.execQuery(sqlCreate)
        }
    }


}
