package jandcode.store;

/**
 * Интерфейс для чтения/записи значения поля.
 * Напрямую не используется, только при реализации новых типов полей.
 */
public interface IStoreFieldValue {

    /**
     * Для использования в реализации типов полей.
     * Установить значение поля в записи
     *
     * @param rawRec сырые данные записи
     * @param rec    с какой записью связаны сырые данные
     * @param value  какое значение пытаются записать
     */
    void setFieldValue(IRawRecord rawRec, StoreRecord rec, Object value);

    /**
     * Для использования в реализации типов полей.
     * Получить значение поля из записи.
     *
     * @param rawRec сырые данные записи
     * @param rec    с какой записью связаны сырые данные
     */
    Object getFieldValue(IRawRecord rawRec, StoreRecord rec);

}
