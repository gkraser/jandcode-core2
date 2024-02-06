package jandcode.core.store;

import jandcode.commons.conf.*;
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

    /**
     * Список имен зарегистрированных вычисляемых полей
     */
    Collection<String> getStoreCalcFieldNames();

    /**
     * Возвращает новую копию конфигурации указанного
     * зарегистрированного, с указанным именем, вычисляемого поля.
     */
    Conf getStoreCalcFieldConf(String name);

}
