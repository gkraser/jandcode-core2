package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Базовый маппер полей с кешированием.
 */
public abstract class BaseFieldsMapper implements IVariantFieldsMapper {

    private Map<String, String> cache = new ConcurrentHashMap<>();

    /**
     * Реализация маппинга
     *
     * @param fieldName      запрашиваемое имя поля
     * @param fieldsOwner    владелец полей
     * @param lowerFieldName запрашиваемое имя поля в lower-case
     * @return имя поля или null, если не получилось маппировать
     * @see IVariantFieldsMapper#mapField(java.lang.String, jandcode.commons.variant.IVariantFieldsOwner)
     */
    protected abstract String mapField(String fieldName, IVariantFieldsOwner fieldsOwner, String lowerFieldName);

    /**
     * Реализация маппинга.
     */
    public String mapField(String fieldName, IVariantFieldsOwner fieldsOwner) {
        String fn = UtStrDedup.lower(fieldName);
        //
        String res = cache.get(fn);
        if (res != null) {
            return res;
        }
        res = mapField(fieldName, fieldsOwner, fn);
        if (res != null) {
            cache.put(fn, res);
        }
        return res;
    }

}
