package jandcode.core.dbm.validate;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

/**
 * Валидаторы для домена
 */
public interface DomainValidatorHolder extends Comp, IModelMember, IDomainMember {

    /**
     * Все зарегистрированные валидаторы
     */
    NamedList<ValidatorDef> getValidators();

    /**
     * Получить все валидаторы указанного поля
     *
     * @param fieldName имя поля
     */
    List<ValidatorDef> getValidatorsField(String fieldName);

    /**
     * Получить все валидаторы для записи, т.е. не привязанные к полям
     */
    List<ValidatorDef> getValidatorsRecord();

}
