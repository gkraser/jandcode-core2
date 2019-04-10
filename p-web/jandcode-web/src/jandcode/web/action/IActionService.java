package jandcode.web.action;

import jandcode.commons.named.*;

/**
 * Интерфейс хранения и управления action
 */
public interface IActionService extends IActionFactory {

    /**
     * Зарегистрированные action.
     * Список отсортирован. Чем больше фасетов у action,
     * тем выше в списке она находится.
     */
    NamedList<ActionDef> getActions();

    /**
     * Создать action по имени. Если не найдена, генерируется ошибка.
     */
    IAction createAction(String name);

    /**
     * Фабрики actions в том порядке, который используется для создания action.
     * Для информационных целей.
     */
    Iterable<IActionFactory> getActionFactorys();

}
