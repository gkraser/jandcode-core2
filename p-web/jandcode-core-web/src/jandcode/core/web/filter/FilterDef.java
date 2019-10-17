package jandcode.core.web.filter;

import jandcode.core.*;

import java.util.*;

/**
 * Объявление filter
 */
public interface FilterDef extends Comp {

    /**
     * Экземпляр обработчика
     */
    IFilter getInst();

    /**
     * Типы фильтра, для которых фильтр предназначен
     */
    Set<FilterType> getTypes();

    /**
     * Включен ли фильтр
     */
    boolean isEnabled();

}
