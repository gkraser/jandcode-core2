package jandcode.core.store;

import jandcode.commons.named.*;

/**
 * Поле для {@link Store}.
 */
public interface StoreField extends INamed {

    /**
     * Специальное значение scale - ничего не делать с double
     */
    int NO_SCALE = Integer.MAX_VALUE;

    /**
     * Тип данных для поля
     */
    StoreDataType getStoreDataType();

    /**
     * Индекс поля
     */
    int getIndex();

    /**
     * Размер поля (для строковых полей).
     */
    int getSize();

    void setSize(int size);

    StoreField size(int size);

    /**
     * Имя словаря, связанного с этим полем.
     */
    String getDict();

    void setDict(String dict);

    StoreField dict(String dict);

    /**
     * До какого знака округлять (для double-полей).
     * Если число отрицательно - округляем целую часть до указанного количества знаков.
     * Например {@code 123 и scale=-2 => 100}.
     * По умолчанию {@link StoreField#NO_SCALE}
     */
    int getScale();

    void setScale(int scale);

    StoreField scale(int scale);

    /**
     * Заголовок поля. Если не указан - имя поля.
     */
    String getTitle();

    void setTitle(String title);

    StoreField title(String title);

}
