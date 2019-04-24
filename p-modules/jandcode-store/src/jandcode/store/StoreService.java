package jandcode.store;

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

}
