package jandcode.store;

/**
 * Интерфейс сырой записи.
 * Используется в реализации полей для чтения/записи данных в запись.
 */
public interface IRawRecord {

    /**
     * Установить внутреннее значение по индексу.
     * index нужно рассматривать как индекс поля в store.
     */
    void setRawValue(int index, Object value);

    /**
     * Получить внутреннее значение по индексу.
     * index нужно рассматривать как индекс поля в store.
     */
    Object getRawValue(int index);

}
