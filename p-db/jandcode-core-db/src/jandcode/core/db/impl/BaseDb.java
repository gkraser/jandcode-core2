package jandcode.core.db.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.stopwatch.*;
import jandcode.core.db.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Реализация интерфейса {@link IDb}
 */
public abstract class BaseDb extends BaseDbConnect implements IDb {

    ////// query

    public DbQuery createQuery(CharSequence sql) {
        return createQuery(sql, null);
    }

    public void execQuery(CharSequence sql) throws Exception {
        execQuery(sql, null);
    }

    public void execQuery(CharSequence sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        try {
            q.exec();
        } finally {
            q.close();
        }
    }

    public int execQueryUpdate(CharSequence sql) throws Exception {
        return execQueryUpdate(sql, null);
    }

    public int execQueryUpdate(CharSequence sql, Object params) throws Exception {
        int res = 0;
        DbQuery q = createQuery(sql, params);
        try {
            res = q.execUpdate();
        } finally {
            q.close();
        }
        return res;
    }

    public DbQuery openQuery(CharSequence sql) throws Exception {
        return openQuery(sql, null);
    }

    public DbQuery openQuery(CharSequence sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        return q;
    }

    ////// query native

    public void execQueryNative(CharSequence sql) throws Exception {
        DbQuery q = createQuery(sql);
        try {
            q.execNative();
        } finally {
            q.close();
        }
    }

    public DbQuery openQueryNative(CharSequence sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        return q;
    }

    ////// tran

    public void rollback(Exception e) throws Exception {
        rollback();
        throw e;
    }

    ////// store

    public Store createStore() {
        StoreService svc = getDbSource().getApp().bean(StoreService.class);
        return svc.createStore();
    }

    public Store createStore(DbQuery query) {
        Store store = createStore();
        for (DbQueryField qf : query.getFields()) {
            String dtn = qf.getDbDataType().getStoreDataTypeName();
            store.addField(qf.getName(), dtn);
        }
        return store;
    }

    public Store loadQuery(Store store, DbQuery query) throws Exception {
        // строим кеш
        int mx = store.getCountFields();
        int[] queryIdx = new int[mx];
        int[] storeIdx = new int[mx];
        int pos = -1;
        for (DbQueryField queryField : query.getFields()) {
            StoreField storeField = store.findField(queryField.getName());
            if (storeField != null) {
                pos++;
                queryIdx[pos] = queryField.getIndex();
                storeIdx[pos] = storeField.getIndex();
            }
        }

        // переносим
        var sw = new DefaultStopwatch("loadQuery");
        sw.start();
        int cnt = 0;
        while (!query.eof()) {
            cnt++;
            StoreRecord rec = store.add();
            for (int i = 0; i <= pos; i++) {
                if (!query.isValueNull(queryIdx[i])) {
                    rec.setValue(storeIdx[i], query.getValue(queryIdx[i]));
                } else {
                    rec.setValue(storeIdx[i], null);
                }
            }
            query.next();
        }
        sw.stop(cnt);
        QueryLogger.log.info(sw.toString());
        //
        return store;
    }

    public Store loadQuery(DbQuery query) throws Exception {
        Store store = createStore(query);
        loadQuery(store, query);
        return store;
    }

    //////

    public Store loadQuery(CharSequence sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.open();
        try (q) {
            return loadQuery(q);
        }
    }

    public Store loadQuery(CharSequence sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        try (q) {
            return loadQuery(q);
        }
    }

    public Store loadQueryNative(CharSequence sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        try (q) {
            return loadQuery(q);
        }
    }

    //////

    public Store loadQuery(Store store, CharSequence sql) throws Exception {
        return loadQuery(store, sql, null);
    }

    public Store loadQuery(Store store, CharSequence sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        try (q) {
            loadQuery(store, q);
        }
        return store;
    }

    public Store loadQueryNative(Store store, CharSequence sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        try (q) {
            loadQuery(store, q);
        }
        return store;
    }

    //////

    public void execScript(List<? extends CharSequence> script, boolean isNative, ErrorCallback onError) throws Exception {
        for (CharSequence sql1 : script) {
            String sql2 = UtString.toString(sql1).trim();
            if (sql2.length() == 0) {
                continue;
            }
            try {
                if (isNative) {
                    execQueryNative(sql2);
                } else {
                    execQuery(sql2);
                }
            } catch (Exception e) {
                if (onError != null) {
                    if (!onError.onErrorCallback(e)) {
                        throw e;
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    public void execScript(CharSequence script, boolean isNative, ErrorCallback onError) throws Exception {
        List<String> sc = UtSql.splitScript(UtString.toString(script));
        execScript(sc, isNative, onError);
    }

}
