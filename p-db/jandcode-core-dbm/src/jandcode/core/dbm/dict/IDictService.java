package jandcode.core.dbm.dict;

import jandcode.commons.named.*;

/**
 * Методы сервиса словарей
 */
public interface IDictService {

    /**
     * Все зарегистрированные словари.
     * Только для чтения.
     */
    NamedList<Dict> getDicts();

    /**
     * Для объектов, которые поддерживают связь со словарями (например store),
     * загружает необходимые словарные данные.
     * <p>
     * Для неизвестных типов объектов ничего не делает.
     *
     * @param data объект с данными, например Store
     */
    void resolveDicts(Object data) throws Exception;

    /**
     * Получить словарь по имени.
     *
     * @param dictName имя словаря
     * @return ошибка, если словаря нет
     */
    default Dict getDict(String dictName) {
        return getDicts().get(dictName);
    }

    /**
     * Загрузить данные словаря.
     * Словарь должен быть загружаемым.
     * см: {@link Dict#isLoadable()}.
     * Возвращается копия данных из кеша.
     *
     * @param dict словарь
     * @return данные
     */
    DictData loadDictData(Dict dict);

    /**
     * см: {@link IDictService#loadDictData(jandcode.core.dbm.dict.Dict)}
     */
    default DictData loadDictData(String dictName) {
        return loadDictData(getDict(dictName));
    }

}
