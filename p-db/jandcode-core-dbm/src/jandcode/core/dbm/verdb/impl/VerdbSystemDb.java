package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.verdb.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Работа с системными таблицами verdb
 */
public class VerdbSystemDb {

    private Mdb mdb;
    private String moduleName;
    private Boolean installed = null;

    /**
     * Создать экземпляр
     *
     * @param mdb        экземпляр Mdb с уже установленным соединением.
     * @param moduleName для какого модуля
     */
    public VerdbSystemDb(Mdb mdb, String moduleName) {
        this.mdb = mdb;
        this.moduleName = moduleName;
    }

    /**
     * Для какого модуля объект
     */
    public String getModuleName() {
        return moduleName;
    }

    //////

    /**
     * Инсталлировать системные таблицы для модуля, если они не инсталлированы
     */
    public void install() throws Exception {
        if (installed != null) {
            return; // уже проверяли
        }

        StoreRecord rec;
        try {
            rec = doLoadRec();
            if (rec == null) {
                doInsertRec();
            }
        } catch (Exception e) {
            doCreateSystemTables();
            doInsertRec();
        }

        installed = true;
    }

    /**
     * Получить версию модуля
     *
     * @return версия
     */
    public VerdbVersion loadVersion() throws Exception {
        install();
        StoreRecord rec = doLoadRec();
        return VerdbVersion.create(rec.getString("ver"));
    }

    /**
     * Записать версию модуля
     */
    public void saveVersion(VerdbVersion ver) throws Exception {
        install();
        doUpdateRec(ver.getText());
    }

    ////// internal

    /**
     * Создать системные таблицы
     */
    protected void doCreateSystemTables() throws Exception {
        String s;
        //
        s = "create table " + VerdbConsts.TABLE_VERDB_INFO +
                " (module_name varchar(64) not null, ver varchar(64))";
        mdb.execQueryNative(s);
        //
        s = "alter table " + VerdbConsts.TABLE_VERDB_INFO +
                " add constraint pk_" + VerdbConsts.TABLE_VERDB_INFO +
                " primary key (module_name)";
        mdb.execQueryNative(s);
    }

    /**
     * Загрузить запись для модуля.
     *
     * @return null, если нет записи для модуля, ошибка - если таблица не существует
     */
    protected StoreRecord doLoadRec() throws Exception {
        String s = "select * from " + VerdbConsts.TABLE_VERDB_INFO +
                " where module_name=:module_name";
        Store st = mdb.loadQuery(s, UtCnv.toMap("module_name", this.moduleName));
        if (st.size() == 0) {
            return null;
        } else {
            return st.get(0);
        }
    }

    /**
     * Добавляет строку для модуля
     */
    protected void doInsertRec() throws Exception {
        Map<String, Object> params = UtCnv.toMap(
                "module_name", this.moduleName,
                "ver", "0.0.0");
        mdb.execQuery("insert into " + VerdbConsts.TABLE_VERDB_INFO +
                        " (module_name, ver) values (:module_name, :ver)",
                params);
    }

    /**
     * Обновляет строку для модуля
     */
    protected void doUpdateRec(String ver) throws Exception {
        Map<String, Object> params = UtCnv.toMap(
                "module_name", this.moduleName,
                "ver", ver);
        mdb.execQuery("update " + VerdbConsts.TABLE_VERDB_INFO +
                        " set ver=:ver where module_name=:module_name",
                params);
    }


}
