package jandcode.core.dbm.fixture;

/**
 * Методы генератора id
 */
public interface IFixtureGenId {

    /**
     * Получить следующий id
     */
    long getNextId();

    /**
     * id, который вернули в последнем вызове nextId
     */
    long getLastId();

    /**
     * Начальная id (включительно). Может быть -1, значить что нижняя
     * граница не установлена.
     */
    long getStartId();

    /**
     * Конечная id (включительно). Может быть -1, значить что верхняя
     * граница не установлена.
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
     * @param endId   по какую. Если -1 - то контроль выхода за диапазон не проводится
     */
    void rangeId(long startId, long endId);

    /**
     * Установить диапазон id, в котором будут выдаватся id.
     *
     * @param startId с какой
     */
    default void rangeId(long startId) {
        rangeId(startId, -1);
    }

}
