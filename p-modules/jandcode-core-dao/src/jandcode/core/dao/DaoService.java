package jandcode.core.dao;

import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

/**
 * Сервис для dao
 */
public interface DaoService extends Comp, IDaoClassFactory {

    /**
     * Получить {@link DaoInvoker} по имени.
     */
    DaoInvoker getDaoInvoker(String name);

    /**
     * Имена зарегистрированных {@link DaoInvoker}
     */
    Collection<String> getDaoInvokerNames();

    /**
     * Получить {@link DaoHolder} по имени
     */
    DaoHolder getDaoHolder(String name);

    /**
     * Имена зарегистрированных {@link DaoHolder}
     */
    Collection<String> getDaoHolderNames();

    /**
     * logger для процесса исполнения dao
     */
    DaoLogger getDaoLogger();

    /**
     * Провайдеры элементов хранилища dao
     */
    NamedList<DaoHolderItemProvider> getDaoHolderItemProviders();

}
