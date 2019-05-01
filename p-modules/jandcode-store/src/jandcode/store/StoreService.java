package jandcode.store;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Сервис для store
 */
public interface StoreService extends Comp, IStoreService {

    /**
     * Список зарегистрированных типов полей
     */
    NamedList<StoreDataType> getStoreDataTypes();

}
