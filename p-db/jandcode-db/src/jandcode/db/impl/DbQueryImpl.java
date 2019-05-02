package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.sql.*;
import jandcode.commons.variant.*;
import jandcode.db.*;

import java.sql.*;
import java.util.*;

public class DbQueryImpl implements DbQuery {

    private DbDriver dbDriver;
    private Connection connection;

    private String sql;
    private String sqlPrepared;
    private List<String> sqlPreparedParams;
    private IVariantNamed params;
    private NamedList<DbQueryField> fields = new DefaultNamedList<>();
    private Statement statement;
    private ResultSet resultSet;
    private QueryLogger queryLogger;
    protected DbDataType.Value[] data;

    // есть данные для строки
    private boolean hasRowData;

    // последняя прочитанная колонка resultSet (особенность jdbc - колонки читаем только подряд)
    private int lastRead = -1;


    public DbQueryImpl(DbDriver dbDriver, Connection connection, String sql, Object params) {
        this.dbDriver = dbDriver;
        this.connection = connection;
        this.queryLogger = new QueryLogger(this);
        setSql(sql);
        setParams(params);
    }

    //////

    public NamedList<DbQueryField> getFields() {
        return fields;
    }

    public void setParams(Object params) {
        this.params = UtCnv.toVariantNamed(params);
    }

    public void setSql(String sql) {
        this.sql = sql;
        this.sqlPrepared = null;
        this.sqlPreparedParams = null;
        if (UtString.empty(sql)) {
            this.sql = "NOT-ASSIGNED-SQL";
        }
        close();
    }

    public void exec() throws Exception {
        try {
            prepareStatement();
            assignStatementParams();
            queryLogger.logStart();
            ((PreparedStatement) statement).execute();
            queryLogger.logStop();
        } catch (Exception e) {
            queryLogger.markError(e);
        }
    }

    public void execNative() throws Exception {
        try {
            createStatement();
            queryLogger.logStart();
            statement.execute(sql);
            queryLogger.logStop();
        } catch (Exception e) {
            queryLogger.markError(e);
        }
    }

    public void open() throws Exception {
        try {
            if (statement != null) {
                throw new XError("Запрос не закрыт");
            }
            prepareStatement();
            assignStatementParams();
            queryLogger.logStart();
            if (!((PreparedStatement) statement).execute()) {
                throw new XError("Запрос не вернул результатов");
            }
            bindResultSet(statement.getResultSet());
            queryLogger.logStop();
        } catch (Exception e) {
            queryLogger.markError(e);
        }
    }

    public void openNative() throws Exception {
        try {
            if (statement != null) {
                throw new XError("Запрос не закрыт");
            }
            createStatement();
            queryLogger.logStart();
            bindResultSet(statement.executeQuery(sql));
            queryLogger.logStop();
        } catch (Exception e) {
            queryLogger.markError(e);
        }
    }

    public void close() {
        if (resultSet != null) {
            try {
                try {
                    resultSet.close();
                } catch (Exception ignore) {
                }
            } finally {
                resultSet = null;
            }
        }
        if (statement != null) {
            try {
                try {
                    statement.close();
                } catch (Exception ignore) {
                }
            } finally {
                statement = null;
            }
        }
        hasRowData = false;
        fields.clear();
    }

    public void next() throws Exception {
        if (!hasRowData) {
            return;
        }
        lastRead = -1;
        hasRowData = resultSet.next();
    }

    public boolean eof() {
        return !hasRowData;
    }

    ////// impl

    protected String getSqlPrepared() {
        if (sqlPrepared == null) {
            SqlParamParser p = new SqlParamParser(this.sql);
            sqlPrepared = p.getResult();
            sqlPreparedParams = p.getParams();
        }
        return sqlPrepared;
    }

    protected void prepareStatement() throws Exception {
        if (statement != null) {
            return;
        }
        statement = connection.prepareStatement(getSqlPrepared());
    }

    protected void createStatement() throws Exception {
        if (statement != null) {
            return;
        }
        statement = connection.createStatement();
    }

    ////// values i/o

    protected void assignStatementParams() throws Exception {
        dbDriver.assignStatementParams((PreparedStatement) statement, params, sqlPreparedParams);
    }

    protected void bindResultSet(ResultSet resultSet) throws Exception {
        if (this.resultSet != null) {
            throw new XError("Запрос не закрыт");
        }
        hasRowData = true;
        this.resultSet = resultSet;
        //
        fields.clear();
        ResultSetMetaData md = resultSet.getMetaData();
        int cols = md.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            DbDataType dt = dbDriver.getDbDataType(md, i);
            //
            String fn = md.getColumnLabel(i);
            if (fields.find(fn) != null) {
                fn = fn + "#" + i;
            }
            //
            DbQueryFieldImpl f = new DbQueryFieldImpl(fn, i - 1, dt);
            fields.add(f);
        }

        data = new DbDataType.Value[cols];
        //

        next();
    }

    protected DbDataType.Value getValueForField(DbQueryField field) {
        try {
            int index = field.getIndex();
            if (lastRead < index) {
                // поле еще не читалось
                // читаем последовательно до нужного
                for (int i = lastRead + 1; i <= index; i++) {
                    DbQueryField vf = fields.get(i);
                    data[i] = vf.getDbDataType().getValue(resultSet, i + 1);
                }
                lastRead = index;
            }
            return data[index];
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }


    ////// access to private

    public String getSql() {
        return sql;
    }

    public List<String> getSqlPreparedParams() {
        return sqlPreparedParams;
    }

    public IVariantNamed getParams() {
        return params;
    }

    public boolean isNativeQuery() {
        return !(statement instanceof PreparedStatement);
    }

    //////  IVariantNamed

    public Object getValue(String name) {
        return getValueForField(fields.get(name)).getValue();
    }

    public boolean isNull(String name) {
        return getValueForField(fields.get(name)).isNull();
    }

    //////

    public Object getValue(int index) {
        return getValueForField(fields.get(index)).getValue();
    }

    public boolean isNull(int index) {
        return getValueForField(fields.get(index)).isNull();
    }

}

