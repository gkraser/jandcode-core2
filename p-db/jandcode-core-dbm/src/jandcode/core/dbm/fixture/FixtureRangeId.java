package jandcode.core.dbm.fixture;

/**
 * Диапазон id
 */
public interface FixtureRangeId {

    /**
     * Начальная id (включительно)
     */
    long getStartId();

    /**
     * Конечная id (включительно)
     */
    long getEndId();

}
