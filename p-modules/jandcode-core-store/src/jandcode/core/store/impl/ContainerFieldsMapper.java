package jandcode.core.store.impl;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Контейнер с мапперами полей.
 * Маппинг производится путем вызова каждого, начиная с конца. Первый, кто вернул поле,
 * используется, остальные игнорируются.
 */
public class ContainerFieldsMapper extends BaseFieldsMapper {

    private List<IVariantFieldsMapper> fieldsMappers = new ArrayList<>();

    public ContainerFieldsMapper() {
    }

    public ContainerFieldsMapper(IVariantFieldsMapper... args) {
        Collections.addAll(this.fieldsMappers, args);
    }

    /**
     * Используемые мапперы
     */
    public List<IVariantFieldsMapper> getFieldsMappers() {
        return fieldsMappers;
    }

    public void add(IVariantFieldsMapper m) {
        if (m != null) {
            this.fieldsMappers.add(m);
        }
    }

    protected String mapField(String fieldName, IVariantFieldsOwner fieldsOwner, String lowerFieldName) {
        for (int i = fieldsMappers.size() - 1; i >= 0; i--) {
            IVariantFieldsMapper fm = fieldsMappers.get(i);
            String res;
            if (fm instanceof BaseFieldsMapper) {
                // избавляемся от двойного кеширования
                res = ((BaseFieldsMapper) fm).mapField(fieldName, fieldsOwner, lowerFieldName);
            } else {
                res = fm.mapField(fieldName, fieldsOwner);
            }
            if (res != null) {
                return res;
            }
        }
        return null;
    }
}
