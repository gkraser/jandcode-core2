package jandcode.core.dbm.store;

import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.store.*;

/**
 * Сервис модели для store
 */
public interface ModelStoreService extends Comp, IModelMember {

    /**
     * Создать пустой store без полей
     */
    Store createStore();


}
