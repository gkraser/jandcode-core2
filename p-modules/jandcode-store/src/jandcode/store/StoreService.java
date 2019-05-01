package jandcode.store;

import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

/**
 * Сервис для store
 */
public interface StoreService extends Comp, IStoreService {

    /**
     * Список имен зарегистрированных типов полей
     */
    Collection<String> getFieldTypes();

    /**
     * Список зарегистрированных типов полей
     */
    NamedList<StoreDataType> getStoreDataTypes();

}
