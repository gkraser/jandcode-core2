package jandcode.core.store;

import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

/**
 * Сервис для store
 */
public interface StoreService extends Comp, IStoreService {

    /**
     * Список зарегистрированных типов полей
     */
    NamedList<StoreDataType> getStoreDataTypes();

    /**
     * Имена зарегистрированных StoreLoader.
     * Фактически имена из конфигурации 'store/storeloader'.
     */
    Collection<String> getStoreLoaderNames();

    /**
     * Есть ли StoreLoader с указанным именем
     */
    boolean hasStoreLoader(String name);

}
