package jandcode.core.dbm.dict;

import jandcode.commons.named.*;
import jandcode.core.store.*;

import java.util.*;

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

    /**
     * По переданному набору ids строит набор ids, который не существует в data
     *
     * @param ids набор id
     * @return набор id, которые отсутсвуют.
     */
    Set<Object> notExistsIds(Set<Object> ids);

    /**
     * Обновить данные указанными.
     * Существующие - обновляются.
     * Не существующие - дополняются.
     *
     * @param newData новый дополнительный набор данных.
     */
    void updateData(Store newData);

}
