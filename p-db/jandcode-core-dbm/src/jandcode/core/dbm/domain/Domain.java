package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Домен
 */
public interface Domain extends Comp, IModelMember, IConfLink,
        IDomain, BeanFactoryOwner {

    /**
     * Поля домена
     */
    NamedList<Field> getFields();

    /**
     * Получить поле по имени.
     *
     * @return error, если не найдено
     */
    default Field getField(String name) {
        return getFields().get(name);
    }

    /**
     * Найти поле по имени
     *
     * @return null, если не найдено
     */
    default Field findField(String name) {
        return getFields().find(name);
    }

    /**
     * Получить поле по имени
     *
     * @return error, если не найдено
     */
    default Field f(String name) {
        return getFields().get(name);
    }

}
