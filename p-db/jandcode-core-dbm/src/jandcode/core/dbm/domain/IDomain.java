package jandcode.core.dbm.domain;

import java.util.*;

/**
 * Набор основных свойств {@link Domain}.
 */
public interface IDomain {

    /**
     * Заголовок
     */
    String getTitle();

    /**
     * Теги.
     * Собираются из личных атрибутов домена tag.XXX. 'tag.' отбрасывается.
     */
    Map<String, String> getTags();

    /**
     * Есть ли указанный тег.
     */
    boolean hasTag(String tag);

    /**
     * Есть ли тег db (является ли объектом базы данных)
     */
    default boolean hasTagDb() {
        return hasTag("db");
    }

    /**
     * Имя таблицы в базе данных для домена.
     * Имеет смысл только для доменов в базе данных и наследников от них.
     */
    String getDbTableName();


}
