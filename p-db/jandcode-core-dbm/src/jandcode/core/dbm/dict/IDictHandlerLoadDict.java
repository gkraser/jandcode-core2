package jandcode.core.dbm.dict;

import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

/**
 * Интерфейс для DictHandler.
 * <p>
 * Словари, обработчики которых имеют такой интерфейс,
 * способны загружатся сразу и полностью. Т.е. они знают полный возможный
 * набор id, которые в словаре допустимы.
 */
public interface IDictHandlerLoadDict {

    /**
     * Загрузить данные для словаря.
     * После выполнения метода ожидается, что в data
     * будут созданы записи для каждой id, про которую словарь знает.
     *
     * @param mdb  соединенная база данных
     * @param dict для какого словаря
     * @param data куда загружать данные словаря.
     */
    void loadDict(Mdb mdb, Dict dict, Store data) throws Exception;


}
