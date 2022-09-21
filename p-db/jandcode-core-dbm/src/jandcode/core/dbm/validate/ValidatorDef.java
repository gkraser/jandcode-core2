package jandcode.core.dbm.validate;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Определение валидатора для домена
 */
public interface ValidatorDef extends Comp, IConfLink {

    /**
     * Создать экземпляр валидатора
     */
    Validator createInst();

    /**
     * Имя поля, для которого предназначен валидатор
     */
    String getFieldName();

}
