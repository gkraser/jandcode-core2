package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Поле домена
 */
public interface Field extends Comp, IConfLink, IDomainMember, IModelMember,
        IField, BeanFactoryOwner {

    /**
     * Порядковый номер поля в домене (начиная с 0).
     */
    int getIndex();

}
