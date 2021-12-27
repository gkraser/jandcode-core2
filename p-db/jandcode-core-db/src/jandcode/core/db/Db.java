package jandcode.core.db;

/**
 * Соединение с базой данных.
 * Может выполнять физические запросы к базе данных, управляет транзакциями.
 */
public interface Db extends IDbSourceMember, IDbConnect, IDb {

}