package jandcode.core.db.std;

import jandcode.commons.error.*;
import jandcode.core.db.*;
import jandcode.core.store.*;

import java.sql.*;
import java.util.*;

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

    public DbQuery createQuery(CharSequence sql, Object params) {
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

    public DbParams getDbParams() {
        return getWrap().getDbParams();
    }

    ////// IDbUtils

    public DbQuery createQuery(CharSequence sql) {
        return getWrapConnected().createQuery(sql);
    }

    public void execQuery(CharSequence sql) throws Exception {
        getWrapConnected().execQuery(sql);
    }

    public void execQuery(CharSequence sql, Object params) throws Exception {
        getWrapConnected().execQuery(sql, params);
    }

    public DbQuery openQuery(CharSequence sql) throws Exception {
        return getWrapConnected().openQuery(sql);
    }

    public DbQuery openQuery(CharSequence sql, Object params) throws Exception {
        return getWrapConnected().openQuery(sql, params);
    }

    public void execQueryNative(CharSequence sql) throws Exception {
        getWrapConnected().execQueryNative(sql);
    }

    public DbQuery openQueryNative(CharSequence sql) throws Exception {
        return getWrapConnected().openQueryNative(sql);
    }

    public void rollback(Exception e) throws Exception {
        getWrap().rollback(e);
    }

    public Store loadQuery(CharSequence sql) throws Exception {
        return getWrapConnected().loadQuery(sql);
    }

    public Store loadQuery(CharSequence sql, Object params) throws Exception {
        return getWrapConnected().loadQuery(sql, params);
    }

    public Store loadQueryNative(CharSequence sql) throws Exception {
        return getWrapConnected().loadQueryNative(sql);
    }

    public Store loadQuery(Store store, CharSequence sql) throws Exception {
        return getWrapConnected().loadQuery(store, sql);
    }

    public Store loadQuery(Store store, CharSequence sql, Object params) throws Exception {
        return getWrapConnected().loadQuery(store, sql, params);
    }

    public Store loadQueryNative(Store store, CharSequence sql) throws Exception {
        return getWrapConnected().loadQueryNative(store, sql);
    }

    public Store createStore() {
        return getWrap().createStore();
    }

    public Store createStore(DbQuery query) {
        return getWrapConnected().createStore(query);
    }

    public Store loadQuery(Store store, DbQuery query) throws Exception {
        return getWrapConnected().loadQuery(store, query);
    }

    public Store loadQuery(DbQuery query) throws Exception {
        return getWrapConnected().loadQuery(query);
    }

    public void execScript(List<? extends CharSequence> script, boolean isNative, ErrorCallback onError) throws Exception {
        getWrapConnected().execScript(script, isNative, onError);
    }

    public void execScript(CharSequence script, boolean isNative, ErrorCallback onError) throws Exception {
        getWrapConnected().execScript(script, isNative, onError);
    }

}
