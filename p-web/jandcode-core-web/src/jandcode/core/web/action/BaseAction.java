package jandcode.core.web.action;

import jandcode.core.*;
import jandcode.core.web.*;

/**
 * Базовый предок для action.
 * Определяет сервисные методы для удобства.
 */
public abstract class BaseAction extends BaseComp implements IAction {

    private RequestUtils requestUtils;

    //////

    /**
     * Реализация {@link IAction}. Этот метод не нужно перекрывать.
     */
    public void exec(Request request) throws Exception {
        this.requestUtils = new RequestUtils(request);
        onExec();
    }

    /**
     * Выполнение action.
     * По умолчанию выполняет метод из атрибута {@link WebConsts#a_actionMethod}.
     * Если значения у атрибута нет, предполагается что его значение 'index'.
     * <p>
     * Можно перекрыть и реализовать собственную обработку.
     */
    protected void onExec() throws Exception {
        getReq().execActionMethod(this, getReq().getActionMethod());
    }

    //////

    /**
     * Запрос {@link Request} и утилиты для него
     */
    public RequestUtils getReq() {
        return requestUtils;
    }

    //////

}
