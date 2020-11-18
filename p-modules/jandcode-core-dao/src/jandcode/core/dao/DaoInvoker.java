package jandcode.core.dao;

import jandcode.core.*;

import java.util.*;

/**
 * Исполнитель dao
 */
public interface DaoInvoker extends Comp, IBeanFactoryOwner, IDaoInvoker {

    /**
     * Список зарегистрированных фильтров.
     * Только для чтения.
     */
    Collection<DaoFilter> getDaoFilters();

}
