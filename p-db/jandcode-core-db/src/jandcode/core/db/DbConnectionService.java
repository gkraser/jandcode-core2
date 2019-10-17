package jandcode.core.db;

import jandcode.core.*;

import java.sql.*;

/**
 * Сервис соединений jdbc. Пул в частности.
 */
public interface DbConnectionService extends Comp, IDbSourceMember {

    /**
     * Возвращает новое (или из пула) установленное соединение
     */
    Connection connect();

    /**
     * Разрыв соединения. Соединение возвращается в пул.
     *
     * @param conn что отключить.
     */
    void disconnect(Connection conn);

    /**
     * Разрыв всех соединений и очистка пула.
     */
    void disconnectAll();

}
