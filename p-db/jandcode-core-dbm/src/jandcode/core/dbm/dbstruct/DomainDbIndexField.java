package jandcode.core.dbm.dbstruct;

import jandcode.commons.named.*;

/**
 * Поле индекса
 */
public interface DomainDbIndexField extends INamed {

    /**
     * Сортировка в обратном порядке.
     * В конфигурации: если имя поля начинается со '*', то desc=true.
     * conf: {@code <dbindex fields="f1,*f2"/>}
     */
    boolean isDesc();

}
