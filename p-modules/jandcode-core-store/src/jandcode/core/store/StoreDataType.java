package jandcode.core.store;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;

/**
 * Тип данных для поля store
 */
public interface StoreDataType extends INamed {

    /**
     * Тип данных (см. {@link VariantDataType}).
     * Нужен для поддержки variant.
     */
    VariantDataType getDataType();

    /**
     * Установить значение поля в записи
     *
     * @param rawRec сырые данные записи
     * @param index  индекс поля для rawRec
     * @param value  какое значение пытаются записать
     * @param rec    с какой записью связаны сырые данные
     * @param field  с каким полем связаны сырые данные
     */
    void setFieldValue(IRawRecord rawRec, int index, Object value, StoreRecord rec, StoreField field);

    /**
     * Для использования в реализации типов полей.
     * Получить значение поля из записи.
     *
     * @param rawRec сырые данные записи
     * @param index  индекс поля для rawRec
     * @param rec    с какой записью связаны сырые данные
     * @param field  с каким полем связаны сырые данные
     */
    Object getFieldValue(IRawRecord rawRec, int index, StoreRecord rec, StoreField field);


}
