package jandcode.core.dbm.dict;

import jandcode.commons.named.*;

import java.util.*;

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
     * @return данные словаря
     */
    DictData loadDictData(Dict dict) throws Exception;

    /**
     * см: {@link IDictService#loadDictData(jandcode.core.dbm.dict.Dict)}
     */
    default DictData loadDictData(String dictName) throws Exception {
        return loadDictData(getDict(dictName));
    }

    /**
     * Загрузить данные словаря для указанного набора id.
     *
     * @param dict словарь
     * @param ids  набор id
     * @return данные словаря
     */
    DictData loadDictData(Dict dict, Collection<Object> ids) throws Exception;

    /**
     * см: {@link IDictService#loadDictData(jandcode.core.dbm.dict.Dict, java.util.Collection)}
     */
    default DictData loadDictData(String dictName, Collection<Object> ids) throws Exception {
        return loadDictData(getDict(dictName), ids);
    }

    /**
     * Получить значение поля словаря для указанного id
     *
     * @param dictName  имя словаря
     * @param id        id в словаре
     * @param fieldName какое поле нужно получить
     * @return null, если значение для id не существует
     */
    Object getDictValue(String dictName, Object id, String fieldName);

    /**
     * Получить значение поля словаря для указанного id. Возвращаентся значение поля по умолчанию
     *
     * @param dictName имя словаря
     * @param id       id в словаре
     * @return null, если значение для id не существует
     */
    Object getDictValue(String dictName, Object id);

    /**
     * Получить значение поля словаря для указанного id в виде строки
     *
     * @param dictName  имя словаря
     * @param id        id в словаре
     * @param fieldName какое поле нужно получить
     * @return пустая строка, если значение для id не существует
     */
    String getDictText(String dictName, Object id, String fieldName);

    /**
     * Получить значение поля словаря для указанного id в виде строки. Возвращаентся значение поля по умолчанию
     *
     * @param dictName имя словаря
     * @param id       id в словаре
     * @return пустая строка, если значение для id не существует
     */
    String getDictText(String dictName, Object id);

}
