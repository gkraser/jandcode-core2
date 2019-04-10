package jandcode.db;

/**
 * Интерфейс для объектов, которые знают про DbSource
 */
public interface IDbSourceLink {

    DbSource getDbSource();

}
