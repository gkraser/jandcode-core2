package jandcode.core.web.action;

import jandcode.core.web.*;

/**
 * Объект, который способен проанализировать запрос и создать action,
 * которая должна его обрабатывать. Для созданной action он может создать необходимое
 * ей окружение в атрибутах запроса.
 */
public interface IActionFactory {

    /**
     * Создать action для запроса.
     *
     * @param request запрос. Допускается модифицировать атрибуты
     * @return null, если нельзя создать action для указанного запроса
     */
    IAction createAction(Request request);


}
