package jandcode.db.test

import jandcode.core.*
import jandcode.core.test.*
import jandcode.db.*
import jandcode.db.impl.*
import jandcode.commons.test.*

class DbSimpleTestSvc extends BaseTestSvc {

    String dbsourceName = "test.1"
    DbService svc
    DbSource dbs
    Db db

    String checkDbDatatype_table = "checkDbType"
    String checkDbDatatype_lastSqltype = ""

    public void setUp() throws Exception {
        super.setUp();
        //
        App app = testSvc(AppTestSvc).app
        svc = app.bean(DbService)
        dbs = svc.getDbSource(dbsourceName)
        //
        if (!app.conf.getBoolean("test:dbprepared")) {
            doPrepare()
            app.conf.setValue("test:dbprepared", true)
        }
        //
        db = dbs.getDb()
        db.connectDirect()
    }

    void tearDown() throws Exception {
        super.tearDown()
        if (db != null) {
            db.disconnect()
        }
    }

    def doPrepare() {
        DbManagerService man = dbs.bean(DbManagerService)
        if (!man.existDatabase()) {
            man.createDatabase()
            // создаем тестовые таблицы
            dbs.db.connect()
            try {
                dbs.db.execQuery("""
                    create table test1 (
                        id integer,
                        s1 varchar(20),
                        s2 varchar(20)
                    )
                """)
                for (i in 1..10) {
                    dbs.db.execQuery("insert into test1 (id,s1,s2) values (:id,:s1,:s2)",
                            [id: i, s1: "s1-${i}", s2: "s2;${i}"])
                }
            } finally {
                dbs.db.disconnect()
            }
        }
    }

    //////

    public void checkTable_checkDbDatatype(String dbdatatype, int size = 20) {
        DbDatatype dbt = db.getDbSource().getDbDriver().getDbDatatypes().get(dbdatatype)
        String sqltype = dbt.getSqlType(size);
        //
        if (checkDbDatatype_lastSqltype != sqltype) {

            println "${db.getDbSource().getDbType()} [${dbdatatype} => ${sqltype}]"

            // сменился тип, таблица не актуальная, пересоздаем
            try {
                db.execQuery("drop table ${checkDbDatatype_table}")
            } catch (e) {
                testSvc(UtilsTestSvc).showError(e)
            }

            db.execQuery("create table ${checkDbDatatype_table} (f1 ${sqltype})")

            checkDbDatatype_lastSqltype = sqltype
        }
    }

    /**
     * Записать dbdatatype (не sql!) значение, прочитать его и вернуть
     */
    public Object dbdatatypeRetrive(String dbdatatype, Object value, int size = 20) throws Exception {
        checkTable_checkDbDatatype(dbdatatype, size)
        Object res = null
        db.execQuery("delete from ${checkDbDatatype_table}")
        db.execQuery("insert into ${checkDbDatatype_table} (f1) values(:v)", [v: value])
        DbQuery q = db.openQuery("select * from ${checkDbDatatype_table}")
        try {
            res = q.getValue("f1")
        } finally {
            q.close()
        }
        return res
    }

    /**
     * Вернуть тип dbdatatype для колонки с типом dbdatatype
     */
    public String dbdatatypeResult(String dbdatatype) throws Exception {
        checkTable_checkDbDatatype(dbdatatype)
        String res = null
        DbQuery q = db.openQuery("select * from ${checkDbDatatype_table}")
        try {
            res = ((DbQueryFieldImpl) q.getFields().get(0)).getDbDatatype().getName()
        } finally {
            q.close()
        }
        return res
    }

}
