package jandcode.core.db;

/**
 * Интерфейс для объектов, которые хотят знать про DbSource
 */
public interface IDbSourceMember extends IDbSourceLink {

    void setDbSource(DbSource dbSource);

}
