package jandcode.db.test

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.test.*
import jandcode.core.store.*
import jandcode.core.test.*
import jandcode.db.*

/**
 * Утилиты для поддержки тестирования модуля db и драйверов на его основе.
 * В setUp создается при необходимости база данных, описанная в dbsource test.1
 * и с ней устанавливается соединение, доступное через db.
 */
class DbSimpleTestSvc extends BaseAppTestSvc {

    String dbsourceName = "test1"

    DbService svc
    DbSource dbSource
    DbDriver dbDriver
    String dbType
    Db db

    String checkDbDataType_table = "checkDbDataType"
    String checkDbDataType_lastSqltype = ""

    void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(DbService)
        dbSource = svc.getDbSource(dbsourceName)
        dbDriver = dbSource.getDbDriver()
        dbType = dbDriver.getDbType()
        db = dbSource.createDb(true)
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
        DbManagerService man = dbSource.bean(DbManagerService)
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
        println "checkSqlTypes for: ${dbDriver.name}"
        for (DbDataType dbt : dbDriver.dbDataTypes) {
            String sqltype = dbt.getSqlType(20)
            if (UtString.empty(sqltype)) {
                println "  ${UtString.padRight(dbt.name, 20)}-> SKIP"
                continue
            }
            checkTable_checkDbDataType(dbt.name)
            checkDbDataType_lastSqltype = ""
        }
    }

    /**
     * Создание полей в store для всех типов dbdatatype для проверки их имен.
     */
    void checkStoreTypes() {
        StoreService storeSvc = app.bean(StoreService)
        //
        println "checkStoreTypes for: ${dbDriver.name}"
        for (DbDataType dbt : dbDriver.dbDataTypes) {
            println "  ${UtString.padRight(dbt.name, 20)}-> ${dbt.storeDataTypeName}"
            Store st = storeSvc.createStore()
            if (UtString.empty(dbt.getStoreDataTypeName())) {
                throw new XError("Для dbdatatype ${dbt.name} не определен storedatatype")
            }
            st.addField("f1", dbt.getStoreDataTypeName())
            st.add(f1: "1")
            st.get(0).getValue("f1")
        }
    }

    /**
     * Проверка для всех полей: null-значения пишутся и читаются корректно.
     */
    void checkNullTypes() {
        println "checkNullTypes for: ${dbDriver.name}"
        for (DbDataType dbt : dbDriver.dbDataTypes) {
            String sqltype = dbt.getSqlType(20)
            if (UtString.empty(sqltype)) {
                println "  ${UtString.padRight(dbt.name, 20)}-> SKIP"
                continue
            }
            dbdatatypeIsNull(dbt.name)
            checkDbDataType_lastSqltype = ""
        }
    }

    /**
     * Проверить наличие таблицы для проверки типа данных и создать ее, если нету
     * @param dbdatatype тип данных
     * @param size размер
     */
    void checkTable_checkDbDataType(String dbdatatype, int size = 20) {
        DbDataType dbt = dbDriver.getDbDataTypes().get(dbdatatype)
        String sqltype = dbt.getSqlType(size);
        //
        if (checkDbDataType_lastSqltype != sqltype) {

            println "${dbDriver.name} [${dbdatatype} => ${sqltype}]"

            // сменился тип, таблица не актуальная, пересоздаем
            try {
                db.execQuery("drop table ${checkDbDataType_table}")
            } catch (e) {
                testSvc(UtilsTestSvc).showError(e)
            }

            //fix: postgresql: ОШИБКА: в кешированном плане не должен изменяться тип результата
            db.disconnect()
            db.connect()

            db.execQuery("create table ${checkDbDataType_table} (f1 ${sqltype})")

            checkDbDataType_lastSqltype = sqltype
        }
    }

    /**
     * Записать dbdatatype значение, прочитать его и вернуть
     */
    public Object dbdatatypeRetrive(String dbdatatype, Object value, int size = 20) throws Exception {
        checkTable_checkDbDataType(dbdatatype, size)
        Object res = null
        db.execQuery("delete from ${checkDbDataType_table}")
        db.execQuery("insert into ${checkDbDataType_table} (f1) values(:v)", [v: value])
        DbQuery q = db.openQuery("select * from ${checkDbDataType_table}")
        try {
            res = q.getValue("f1")
        } finally {
            q.close()
        }
        return res
    }

    /**
     * Записать dbdatatype значение null, и вернуть isNull
     */
    public boolean dbdatatypeIsNull(String dbdatatype) throws Exception {
        checkTable_checkDbDataType(dbdatatype, 20)
        boolean res
        db.execQuery("delete from ${checkDbDataType_table}")
        db.execQuery("insert into ${checkDbDataType_table} (f1) values(:v)", [v: null])
        DbQuery q = db.openQuery("select * from ${checkDbDataType_table}")
        try {
            res = q.isNull("f1")
        } finally {
            q.close()
        }
        return res
    }

    /**
     * Вернуть тип dbdatatype для колонки с типом dbdatatype
     */
    public String dbdatatypeResult(String dbdatatype) throws Exception {
        checkTable_checkDbDataType(dbdatatype)
        String res = null
        DbQuery q = db.openQuery("select f1 from ${checkDbDataType_table}")
        try {
            res = q.getFields().get(0).getDbDataType().getName()
        } finally {
            q.close()
        }
        return res
    }

}
