package jandcode.core.db.impl;

import jandcode.commons.error.*;
import jandcode.core.db.*;

import java.sql.*;

/**
 * Реализация интерфейса {@link IDbConnect}
 */
public abstract class BaseDbConnect extends BaseDbSourceMember implements IDbConnect {

    private Connection connection;
    protected int connectLevel;
    protected int trnLevel;
    private DbConnectionService connectionService;

    ////// connect

    protected void setConnectionService(DbConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public void connect() throws Exception {
        if (connectLevel == 0) {
            connection = connectionService.connect();
        }
        connectLevel++;
    }

    public void disconnect() throws Exception {
        connectLevel--;
        if (connectLevel <= 0) {
            connectLevel = 0;
            try {
                connectionService.disconnect(connection);
            } finally {
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

    /**
     * Находится ли база в транзакции
     *
     * @return true, если транзакция активна
     */
    public boolean isTran() {
        return trnLevel > 0;
    }

}
