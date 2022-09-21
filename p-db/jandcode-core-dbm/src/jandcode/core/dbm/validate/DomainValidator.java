package jandcode.core.dbm.validate;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

/**
 * Валидаторы для домена
 */
public interface DomainValidator extends Comp, IModelMember, IDomainMember {

    /**
     * Зарегистрированные валидаторы
     */
    NamedList<ValidatorDef> getValidators();

}
