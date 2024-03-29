package jandcode.core.dbm.mdb;

import jandcode.core.dbm.genid.*;

/**
 * Методы для genid
 */
public interface IMdbGenId {

    /**
     * Возвращает генератор по имени.
     * <p>
     * Генератор привязан к текущему экземпляру {@link jandcode.core.dbm.mdb.Mdb},
     * т.е. все запросы будут выполнятся через текущее соединение.
     *
     * @param genIdName имя генератора
     * @return ошибка, если генератора нет
     */
    GenId getGenId(String genIdName);

    /**
     * Возвращает генератор с кешем по имени.
     * Такой генератор выделяет id блоками размером cacheSize (в штуках).
     * Это позволяет увеличить скорость массовой вставки данных.
     * <p>
     * Не все драйвера genid могут поддерживать такой функционал,
     * поэтому, если драйвер не поддерживает кеш, то будет возвращает обычный
     * генератор и кеш будет проигнорирован.
     * <p>
     * Генератор привязан к текущему экземпляру {@link jandcode.core.dbm.mdb.Mdb},
     * т.е. все запросы будут выполнятся через текущее соединение.
     *
     * @param genIdName имя генератора
     * @param cacheSize размер кеша
     * @return генератор
     */
    GenId getGenId(String genIdName, long cacheSize);

    /**
     * Возвращает следующее уникальное значение для указанного генератора
     */
    long getNextId(String genIdName);

}
