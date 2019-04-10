package jandcode.web.filter;

import jandcode.commons.named.*;

/**
 * Интерфейс сервиса фильтров для web
 */
public interface IFilterService {

    /**
     * Зарегистрированные фильтры
     */
    NamedList<FilterDef> getFilters();

}
