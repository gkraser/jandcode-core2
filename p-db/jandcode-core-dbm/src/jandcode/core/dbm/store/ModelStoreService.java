package jandcode.core.dbm.store;

import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;

/**
 * Сервис модели для store
 */
public interface ModelStoreService extends Comp, IModelMember {

    /**
     * Создать пустой store без полей
     */
    Store createStore();

    /**
     * Создать пустой store со структурой как в домене.
     *
     * @param domain для какого домена
     */
    Store createStore(Domain domain);

}
