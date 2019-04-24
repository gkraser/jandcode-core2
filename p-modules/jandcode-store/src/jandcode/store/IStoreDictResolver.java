package jandcode.store;

/**
 * Интерфейс для полдучения значений словарных полей.
 */
public interface IStoreDictResolver {

    /**
     * Возврашает значение для словаря
     *
     * @param dictName      для какого словаря
     * @param key           ключ в словаре
     * @param dictFieldName поле в словаре. Если не указано, то расчитываем на
     *                      поле по умолчанию.
     * @return значение поля словаря или null, если не найдено
     */
    Object getDictValue(String dictName, Object key, String dictFieldName);

}
