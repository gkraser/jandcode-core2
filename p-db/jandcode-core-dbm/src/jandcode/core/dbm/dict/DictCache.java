package jandcode.core.dbm.dict;

/**
 * Кеш данных словарей
 */
public interface DictCache {

    /**
     * Получить закешированные данные для словаря.
     * Словарб должен быть загружаемым.
     * см: {@link Dict#isLoadable()}.
     *
     * @param dict для какого словаря
     * @return данные словаря или ошибка, если словарь не имеет кеша
     */
    DictData getDictData(Dict dict);

    /**
     * Уведомить кеш, что словарь dict изменился и его нужно перезагрузить.
     *
     * @param dict какой словарь поменялся
     */
    void invalidate(Dict dict);

}
