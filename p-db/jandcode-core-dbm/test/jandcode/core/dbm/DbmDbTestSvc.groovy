package jandcode.core.dbm

import jandcode.commons.*
import jandcode.core.db.*
import jandcode.core.dbm.mdb.*
import jandcode.core.store.*
import jandcode.core.test.*

/**
 * Утилиты для поддержки тестирования dbm в контексте реальной базы данных
 */
class DbmDbTestSvc extends AppTestSvc {

    /**
     * Имя модели, для которой предназначена вся работа
     */
    String modelName = "test1"

    Model model
    Db db
    Mdb mdb

    void setUp() throws Exception {
        super.setUp();
        //
        model = app.bean(ModelService).getModel(modelName)
        db = model.dbSource.createDb(true)
        //
        String cfgKey = "test/${getClass().getName()}/dbprepare/${modelName}/prepared"
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

    void doPrepare() {
        DbManagerService man = model.dbSource.bean(DbManagerService)
        if (!man.existDatabase()) {
            utils.delim("create database")
            utils.outMap(man.dbSource.props)
            man.createDatabase()
            utils.delim()
        } else {
            utils.delim("use exists database")
            utils.outMap(man.dbSource.props)
            utils.delim()
        }
    }

    Mdb getMdb() {
        if (this.mdb == null) {
            this.mdb = model.createMdb(db)
        }
        return this.mdb
    }

    /**
     * Создать пустой store
     */
    Store createStore() {
        return app.bean(StoreService).createStore()
    }

    /**
     * Удалить таблицу
     */
    void dropTable(String tableName) {
        try {
            db.execQuery("drop table ${tableName}")
        } catch (ignored) {
            // ignore
        }
    }

    /**
     * Создать таблицу в базе со структорой как у store и данными как у store.
     * Без индексов.
     * @param tableName имя создаваемой таблицы. Если существует - уничтожается
     * @param store структура и данные для таблицы
     */
    void createTable(String tableName, Store store) {
        dropTable(tableName)
        //
        DbDriver drv = db.dbSource.dbDriver
        StringBuilder sbCreate = new StringBuilder()
        for (StoreField f in store.fields) {
            DbDataType dbt = drv.getDbDataTypes().get(f.storeDataType.name)
            String s1 = dbt.getSqlType(f.getSize())
            if (sbCreate.length() != 0) {
                sbCreate.append(",\n")

            }
            sbCreate.append("  ${f.name} ${s1}")
        }
        String ddl = "create table ${tableName} (\n${sbCreate}\n)"
        println ddl
        db.execQueryNative(ddl)
        //
        def sqlIns = "insert into ${tableName} (" +
                store.fields.collect({ f -> f.name }).join(',') + ") values (" +
                store.fields.collect({ f -> ":${f.name}" }).join(',') + ")"
        def q = db.createQuery(sqlIns)

        for (StoreRecord rec in store) {
            q.setParams(rec)
            q.exec()
        }

        println "create table: ${tableName}, records: ${store.size()}, fields: ${UtCnv.toNameList(store.fields).join(',')}"
    }


}
