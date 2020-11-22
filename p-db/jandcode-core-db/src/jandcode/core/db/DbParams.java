package jandcode.core.db;

import java.sql.*;

/**
 * Некоторые параметры для управления поведением {@link Db} и {@link DbQuery}.
 */
public interface DbParams {

    /**
     * см: {@link Statement#getFetchSize()}.
     * По умолчанию установлено в значение -1,
     * что означает - ничего не делать, драйвер работает по умолчанию, как настроен.
     */
    int getFetchSize();

    /**
     * см: {@link Statement#setFetchSize(int)}.
     */
    void setFetchSize(int rows);

}
