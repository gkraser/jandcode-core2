package jandcode.web.action;

import java.util.*;

/**
 * Провайдер для action.
 * Его цель - загрузить набор ActionDef по его правилам.
 */
public interface IActionProvider {

    /**
     * Загрузить actions.
     * Возвращает null, если нет доступных объектов для загрузки.
     */
    List<ActionDef> loadActions() throws Exception;

}
