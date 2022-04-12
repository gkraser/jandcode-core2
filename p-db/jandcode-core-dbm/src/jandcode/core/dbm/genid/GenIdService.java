package jandcode.core.dbm.genid;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Сервис уникальных id.
 */
public interface GenIdService extends Comp, IModelMember {

    /**
     * Используемые в генераторах драйвера.
     */
    NamedList<GenIdDriver> getDrivers();

    /**
     * Зарегистрированные генераторы.
     */
    NamedList<GenId> getGenIds();

    /**
     * Возвращает генератор по имени
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
     *
     * @param genIdName имя генератора
     * @param cacheSize размер кеша
     * @return генератор
     */
    GenId getGenId(String genIdName, long cacheSize);

    /**
     * Установить для генератора текущее значение
     * (см: {@link GenId#getCurrentId()}.
     *
     * @param genIdName имя генератора
     * @param value     устанавливаемое значение. Возможно реально будет установлено
     *                  другое значение, если генератор имеет шаг, не равный 1.
     */
    void updateCurrentId(String genIdName, long value);

}
