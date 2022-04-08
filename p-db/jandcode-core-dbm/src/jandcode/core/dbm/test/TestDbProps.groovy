package jandcode.core.dbm.test

import groovy.transform.*
import jandcode.core.db.*
import jandcode.core.dbm.mdb.*
import jandcode.core.store.*

/**
 * Работа со произвольными свойствами, которые хранятся в базе данных
 * для нужд тестов.
 */
@CompileStatic
class TestDbProps extends BaseMdbUtils {

    /**
     * Имя таблицы, где хранятся свойства
     */
    public static final String TABLE_NAME = "jandcode_dbm_test_props_3736675"

    private Boolean hasTable = null

    TestDbProps(Mdb mdb) {
        super(mdb)
    }

    private StoreRecord loadRec(String key) {
        if (!hasTable()) {
            return null
        }
        return mdb.loadQueryRecord("select * from ${TABLE_NAME} where k=:k", [k: key], false)
    }

    private boolean hasTable() {
        if (this.hasTable != null) {
            return this.hasTable
        }
        def dbMeta = mdb.model.dbSource.bean(DbMetadataService)
        this.hasTable = dbMeta.hasTable(TABLE_NAME)
        return this.hasTable
    }

    private void createTable() {
        this.hasTable = null
        mdb.execQueryNative("""
            create table ${TABLE_NAME} (
                k varchar(64) not null,
                v varchar(255)
            )
        """)
    }

    /**
     * Получить значение
     * @param key ключ
     * @return значение. Ели значения нет - возвращается null
     */
    String getProp(String key) {
        StoreRecord rec = loadRec(key)
        if (rec == null) {
            return null
        }
        return rec.getString("v")
    }

    /**
     * Установить значение
     * @param key ключ
     * @param value значение
     */
    void setProp(String key, String value) {
        if (!hasTable()) {
            createTable()
        }
        StoreRecord rec = loadRec(key)
        if (rec == null) {
            mdb.execQuery("insert into ${TABLE_NAME} (k,v) values (:k,:v)", [k: key, v: value])
        } else {
            mdb.execQuery("update ${TABLE_NAME} set v=:v where k=:k", [k: key, v: value])
        }
    }

    /**
     * Сбросить кеш
     */
    void reset() {
        this.hasTable = null
    }

}
