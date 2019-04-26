package jandcode.db.impl;

import jandcode.commons.error.*;
import jandcode.db.*;

import java.sql.*;

public class DbImpl extends BaseDbSourceMember implements Db {

    private Connection connection;
    protected int connectLevel;
    private boolean directConnection;
    protected int trnLevel;

    ////// connect

    protected DbConnectionService getConnectionService(boolean direct) {
        if (direct) {
            return (DbConnectionService) getDbSource().bean(DbConsts.BEAN_DIRECT_CONNECT);
        } else {
            return getDbSource().bean(DbConnectionService.class);
        }
    }

    public void connect() throws Exception {
        if (connectLevel == 0) {
            connection = getConnectionService(false).connect();
        }
        connectLevel++;
    }

    public void connectDirect() throws Exception {
        if (connectLevel == 0) {
            connection = getConnectionService(true).connect();
            directConnection = true;
        }
        connectLevel++;
    }

    public void disconnect() throws Exception {
        connectLevel--;
        if (connectLevel <= 0) {
            connectLevel = 0;
            try {
                getConnectionService(directConnection).disconnect(connection);
            } finally {
                directConnection = false;
                connection = null;
            }
        }
    }

    public void disconnectForce() throws Exception {
        connectLevel = 0;
        disconnect();
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new XError("Not connected");
        }
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    ////// query

    public DbQuery createQuery(String sql, Object params) {
        return new DbQueryImpl(this.getDbSource().getDbDriver(), getConnection(), sql, params);
    }

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

    /**
     * Генерация ошибки, если нет активной транзакции
     */
    protected void checkActiveTrn() throws Exception {
        if (!isTran()) {
            throw new XError("Нет активной транзакции");
        }
    }

    public void startTran() throws Exception {
        if (trnLevel == 0) {
            getConnection().setAutoCommit(false);
        }
        trnLevel++;
    }

    public void commit() throws Exception {
        getConnection(); // проверка на соединение
        checkActiveTrn();
        trnLevel--;
        if (trnLevel <= 0) {
            trnLevel = 0;
            try {
                getConnection().commit();
            } catch (Exception e) {
                getConnection().rollback();
                throw e;
            } finally {
                getConnection().setAutoCommit(true);
            }
        }
    }

    public void rollback() throws Exception {
        getConnection(); // проверка на соединение
        try {
            if (trnLevel != 0) {
                getConnection().rollback();
            }
        } finally {
            if (trnLevel != 0) {
                trnLevel = 0;
                getConnection().setAutoCommit(true);
            }
        }
    }

    public void rollback(Exception e) throws Exception {
        rollback();
        throw e;
    }

    /**
     * Находится ли база в транзакции
     *
     * @return true, если транзакция активна
     */
    public boolean isTran() {
        return trnLevel > 0;
    }

}
