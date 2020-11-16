package jandcode.core.db.std;

import jandcode.core.db.*;
import jandcode.core.store.*;

import java.sql.*;

/**
 * Базовая обстрактная обертка для Db.
 */
public abstract class BaseDbWrapper implements Db {

    /**
     * Какой объект Db обернут
     */
    protected abstract Db getWrap();

    /**
     * Какой объект Db обернут.
     * Подразумевается, что тот, кто этот метод дергает, хочет Db
     * с установленным соединением.
     */
    protected Db getWrapConnected() {
        return getWrap();
    }

    ////// DbSource

    public void setDbSource(DbSource dbSource) {
        // ignore
    }

    public DbSource getDbSource() {
        return getWrap().getDbSource();
    }

    //////  IDbConnect

    public void connect() throws Exception {
        getWrap().connect();
    }

    public void disconnect() throws Exception {
        getWrap().disconnect();
    }

    public void disconnectForce() throws Exception {
        getWrap().disconnectForce();
    }

    public Connection getConnection() {
        return getWrap().getConnection();
    }

    public boolean isConnected() {
        return getWrap().isConnected();
    }

    public DbQuery createQuery(String sql, Object params) {
        return getWrapConnected().createQuery(sql, params);
    }

    public void startTran() throws Exception {
        getWrap().startTran();
    }

    public void commit() throws Exception {
        getWrap().commit();
    }

    public void rollback() throws Exception {
        getWrap().rollback();
    }

    public boolean isTran() {
        return getWrap().isTran();
    }

    ////// IDbUtils

    public DbQuery createQuery(String sql) {
        return getWrapConnected().createQuery(sql);
    }

    public void execQuery(String sql) throws Exception {
        getWrapConnected().execQuery(sql);
    }

    public void execQuery(String sql, Object params) throws Exception {
        getWrapConnected().execQuery(sql, params);
    }

    public DbQuery openQuery(String sql) throws Exception {
        return getWrapConnected().openQuery(sql);
    }

    public DbQuery openQuery(String sql, Object params) throws Exception {
        return getWrapConnected().openQuery(sql, params);
    }

    public void execQueryNative(String sql) throws Exception {
        getWrapConnected().execQueryNative(sql);
    }

    public DbQuery openQueryNative(String sql) throws Exception {
        return getWrapConnected().openQueryNative(sql);
    }

    public void rollback(Exception e) throws Exception {
        getWrap().rollback(e);
    }

    public Store loadQuery(String sql) throws Exception {
        return getWrapConnected().loadQuery(sql);
    }

    public Store loadQuery(String sql, Object params) throws Exception {
        return getWrapConnected().loadQuery(sql, params);
    }

    public Store loadQueryNative(String sql) throws Exception {
        return getWrapConnected().loadQueryNative(sql);
    }

    public void loadQuery(Store store, String sql) throws Exception {
        getWrapConnected().loadQuery(store, sql);
    }

    public void loadQuery(Store store, String sql, Object params) throws Exception {
        getWrapConnected().loadQuery(store, sql, params);
    }

    public void loadQueryNative(Store store, String sql) throws Exception {
        getWrapConnected().loadQueryNative(store, sql);
    }
}
