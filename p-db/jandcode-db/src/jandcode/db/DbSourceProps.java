package jandcode.db;

import jandcode.commons.variant.*;

/**
 * Свойства {@link DbSource}.
 * Это map, где в значениях могут использоватся подстановки '${prop}',
 * где prop - значение другого свойства.
 */
public interface DbSourceProps extends IVariantMap {

    /**
     * Нераскрытое свойство
     */
    Object getRaw(String key);

    /**
     * Возвращает свойства с указанным префиксом. Префикс удаляется.
     *
     * @param prefix часть имени свойства до точки. Например для префикса 'system'
     *               подходит свойство 'system.database' и свойство будет возвращено
     *               с именем 'database'
     * @param raw    true - оригинальное значение, без раскрытия подстановок ${prop}
     */
    IVariantMap subMap(String prefix, boolean raw);

    /**
     * См. {@link DbSourceProps#subMap(java.lang.String, boolean)},
     * где raw=false
     */
    default IVariantMap subMap(String prefix) {
        return subMap(prefix, false);
    }

}
