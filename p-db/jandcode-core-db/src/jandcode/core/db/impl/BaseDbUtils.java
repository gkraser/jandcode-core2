package jandcode.core.db.impl;

import jandcode.core.db.*;
import jandcode.core.db.utils.*;
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

    protected Store internalLoadQuery(DbQuery q) throws Exception {
        Store res;
        try (q) {
            res = StoreUtils.createStoreForQuery(q);
            StoreUtils.loadStoreFromQuery(res, q);
        }
        return res;
    }

    public Store loadQuery(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.open();
        return internalLoadQuery(q);
    }

    public Store loadQuery(String sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        return internalLoadQuery(q);
    }

    public Store loadQueryNative(String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        return internalLoadQuery(q);
    }

    //////

    public void loadQuery(Store store, String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.open();
        try (q) {
            StoreUtils.loadStoreFromQuery(store, q);
        }
    }

    public void loadQuery(Store store, String sql, Object params) throws Exception {
        DbQuery q = createQuery(sql, params);
        q.open();
        try (q) {
            StoreUtils.loadStoreFromQuery(store, q);
        }
    }

    public void loadQueryNative(Store store, String sql) throws Exception {
        DbQuery q = createQuery(sql);
        q.openNative();
        try (q) {
            StoreUtils.loadStoreFromQuery(store, q);
        }
    }

    //////


}
