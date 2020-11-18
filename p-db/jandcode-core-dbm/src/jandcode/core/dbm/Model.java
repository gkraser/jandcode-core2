package jandcode.core.dbm;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.mdb.*;

/**
 * Модель.
 */
public interface Model extends Comp, BeanFactoryOwner, IConfLink,
        IModelDbService, IMdbService {

    /**
     * Кто создал эту модель
     */
    ModelDef getModelDef();

}
