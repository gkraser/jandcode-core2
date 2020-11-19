package jandcode.core.dbm.dict;

import jandcode.core.dbm.mdb.*;

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
     * @param mdb      соединенная база данных
     * @param dictData данные словаря. Заполнить нужно
     *                 {@link DictData#getData()}.
     * @param ids      набор id, которые нужно получить.
     */
    void resolveIds(Mdb mdb, DictData dictData, Collection ids) throws Exception;

}
