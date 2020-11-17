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
     * Имя таблицы в базе данных для домена.
     * Имеет смысл только для доменов в базе данных и наследников от них.
     * <p>
     * При загрузке структуры проставляется в имя домена, если у конфигурации
     * домена имеется атрибут tag.db=true или tag.dbview=true и явно не установлено
     * значение в атрибуте dbtablename.
     */
    String getDbTableName();


}
