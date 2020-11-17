package jandcode.core.dbm;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.db.*;

/**
 * Модель.
 */
public interface Model extends Comp, BeanFactoryOwner, IConfLink,
        IModelDbService {

    /**
     * Кто создал эту модель
     */
    ModelDef getModelDef();

}
