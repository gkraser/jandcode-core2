package jandcode.core.store;

/**
 * Обработчик вычисляемого поля для store
 */
public interface StoreCalcField {

    /**
     * Вычислить значение вычисляемого поля
     *
     * @param field  для какого поля
     * @param record для какой записи
     * @return вычисленное значение
     */
    Object calcValue(StoreField field, StoreRecord record);

}
