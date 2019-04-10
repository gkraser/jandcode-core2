package jandcode.web.action;

import jandcode.web.*;
import jandcode.web.render.*;

/**
 * action. Обработчик web-запросов.
 * Цель обработки - получить некоторый объект с данными и выполнить метод
 * {@link Request#render(java.lang.Object)},
 * передав ему полученный объект. Этот объект будет позже отрендерен
 * подходящим для него классом {@link IRender}.
 */
public interface IAction {

    /**
     * Выполнить
     *
     * @param request для какого запроса. Этот запрос всегда совпадает с
     *                {@link WebService#getRequest()}
     */
    void exec(Request request) throws Exception;

}
