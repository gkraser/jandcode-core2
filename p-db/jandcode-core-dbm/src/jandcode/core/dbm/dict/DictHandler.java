package jandcode.core.dbm.dict;

import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Интерфейс для классов-обрабтчиков словарей.
 */
public interface DictHandler {

    /**
     * Получить данные для словаря.
     * Полсе выполнения метода ожидается, что в store
     * будут созданы записи для каждой id, переданных в ids.
     *
     * @param mdb  соединенная база данных
     * @param dict для какого словаря
     * @param data куда загружать данные словаря.
     * @param ids  набор id, которые нужно получить.
     */
    void resolveIds(Mdb mdb, Dict dict, Store data, Collection ids) throws Exception;

}
