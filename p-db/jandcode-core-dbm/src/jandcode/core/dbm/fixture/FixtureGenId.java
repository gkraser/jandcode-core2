package jandcode.core.dbm.fixture;

/**
 * Генератор id
 */
public interface FixtureGenId {

    /**
     * Получить следующий id
     */
    long getNextId();

    /**
     * Текущий id (который вернули в последнем вызове nextId).
     */
    long getCurId();

    /**
     * Начальная id (включительно)
     */
    long getStartId();

    /**
     * Конечная id (включительно)
     */
    long getEndId();

    /**
     * Пропускает count id. Возвращает первую в пропущенном диапазоне.
     *
     * @param count сколько пропустить
     */
    long skipId(long count);

    /**
     * Установить диапазон id, в котором будут выдаватся id.
     *
     * @param startId с какой
     * @param endId   по какую
     */
    void rangeId(long startId, long endId);

}
