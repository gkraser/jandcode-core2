package jandcode.core.store.impl;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Простой маппер имен полей.
 * Настраивается через map вида "запрашиваемое-поле" -> "реальное-поле".
 */
public class MapFieldsMapper extends BaseFieldsMapper {

    private Map<String, String> mapFields = new HashMap<>();

    public MapFieldsMapper() {
    }

    public MapFieldsMapper(Map<String, String> mapFields) {
        if (mapFields != null) {
            this.mapFields.putAll(mapFields);
        }
    }

    /**
     * Map с соотвествиями полей. Ключ - запрашиваемое поле, значение - поле в store
     */
    public Map<String, String> getMapFields() {
        return mapFields;
    }

    protected String mapField(String fieldName, IVariantFieldsOwner fieldsOwner, String lowerFieldName) {
        String res = mapFields.get(lowerFieldName);
        if (res != null) {
            return res;
        }
        return null;
    }

}
