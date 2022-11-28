package jandcode.core.dbm.validate;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Определение валидатора
 */
public interface ValidatorDef extends Comp, IConfLink {

    /**
     * Создать экземпляр валидатора
     */
    Validator createInst();

    /**
     * Класс валидатора
     */
    Class getCls();

}
