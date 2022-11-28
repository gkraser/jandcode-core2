package jandcode.core.dbm;

/**
 * Константы для dbm.
 */
public class DbmConsts {

    /**
     * Имя модели, dbsource и т.д. по умолчанию.
     * Вообщем для списковых объектов, среди которых существует некоторый
     * "по умолчанию", он имеет такое имя.
     */
    public static final String DEFAULT = "default";

    /**
     * Имя базового объекта по умолчанию (для домена, поля ...).
     * Вообщем для списковых объектов, среди которых существует некоторый
     * "базовый", он имеет такое имя.
     */
    public static final String BASE = "base";

    /**
     * Модель core, которая всегда включается в любую другую модель первой
     */
    public static final String CORE_MODEL = "jandcode.core.dbm";

    /**
     * Имя свойства store, в котором хранится ссылка на домен, если store
     * создается по домену. Начинается с '$', что бы не учавствовало в сериализации store
     * в json.
     */
    public static final String STORE_PROP_DOMAIN = "$domain";

}
