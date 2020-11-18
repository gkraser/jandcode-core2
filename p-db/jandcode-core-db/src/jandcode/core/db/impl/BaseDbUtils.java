package jandcode.core.db.impl;

import jandcode.core.db.*;
import jandcode.core.store.*;

/**
 * Реализация интерфейса {@link IDbUtils}
 */
public abstract class BaseDbUtils extends BaseDbConnect implements IDbUtils {

    ////// query

    public DbQuery createQuery(String sql) {
        return createQuery(sql, null);
    }

    public void execQuery(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        try {
            q.exec();
        } finally {
            q.close();
        }
    }

    public void execQuery(String sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        try {
            q.exec();
        } finally {
            q.close();
        }
    }

    public DbQuery openQuery(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.open();
        return q;
    }

    public DbQuery openQuery(String sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        return q;
    }

    ////// query native

    public void execQueryNative(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        try {
            q.execNative();
        } finally {
            q.close();
        }
    }

    public DbQuery openQueryNative(String sql) throws Exception {
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

    public void loadQuery(Store store, DbQuery query) throws Exception {
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
        while (!query.eof()) {
            StoreRecord rec = store.add();
            for (int i = 0; i <= pos; i++) {
                if (!query.isNull(queryIdx[i])) {
                    rec.setValue(storeIdx[i], query.getValue(queryIdx[i]));
                } else {
                    rec.setValue(storeIdx[i], null);
                }
            }
            query.next();
        }
        //
    }

    public Store loadQuery(DbQuery query) throws Exception {
        Store store = createStore(query);
        loadQuery(store, query);
        return store;
    }

    //////

    public Store loadQuery(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.open();
        try (q) {
            return loadQuery(q);
        }
    }

    public Store loadQuery(String sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        try (q) {
            return loadQuery(q);
        }
    }

    public Store loadQueryNative(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        try (q) {
            return loadQuery(q);
        }
    }

    //////

    public void loadQuery(Store store, String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.open();
        try (q) {
            loadQuery(store, q);
        }
    }

    public void loadQuery(Store store, String sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        try (q) {
            loadQuery(store, q);
        }
    }

    public void loadQueryNative(Store store, String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        try (q) {
            loadQuery(store, q);
        }
    }

    //////


}
