package jandcode.core.dbm.dict;

import jandcode.commons.named.*;
import jandcode.core.store.*;

/**
 * Данные словаря.
 * Имя объекта = имя словаря.
 */
public interface DictData extends INamed {

    /**
     * Для какого словаря
     */
    Dict getDict();

    /**
     * Данные. Структура store соотвествует структуре домена словаря.
     */
    Store getData();

}
