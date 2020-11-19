package jandcode.core.dbm.dict;

import jandcode.core.store.*;

import java.util.*;

/**
 * Интерфейс для dao, которые могут ресолвить словари.
 * Метод resolveDict должен быть dao-методом (помечен аннотацией @DaoMethod).
 */
public interface IResolveDict {

    /**
     * Получить данные для словаря.
     * Полсе выполнения метода ожидается, что в store
     * будут созданы записи для каждой id, переданных в ids.
     *
     * @param dict для какого словаря
     * @param data куда записывать. Структура store соотвествует структуре домена словаря.
     * @param ids  набор id, которые нужно получить.
     */
    void resolveDict(Dict dict, Store data, Collection ids);

}
