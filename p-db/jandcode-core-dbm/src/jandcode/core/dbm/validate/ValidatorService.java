package jandcode.core.dbm.validate;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Сервис поддержки валидаторов
 */
public interface ValidatorService extends Comp, IModelMember {

    /**
     * Зарегистрированные валидаторы
     */
    NamedList<ValidatorDef> getValidators();

    /**
     * Создать определение валидатора по конфигурации
     */
    ValidatorDef createValidatorDef(Conf conf);

}
