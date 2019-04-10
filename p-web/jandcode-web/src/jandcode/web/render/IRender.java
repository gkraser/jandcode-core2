package jandcode.web.render;

import jandcode.web.*;

/**
 * render.
 * Цель объекта - отрендерить объект data, который был получен в action.
 * Рендеринг текстовых данных нужно осуществлять в {@link Request#getOutWriter()},
 * а бинарных в {@link Request#getOutStream()}.
 * <p>
 * Если нужно установить contentType или какие-то заголовки http-ответа,
 * то их нужно установить до записи в потоки вывода, т.к. они ссылаются на реальные
 * потоки http-ответа.
 */
public interface IRender {

    /**
     * Отрендерить объект
     *
     * @param data    какой объект
     * @param request для какого запроса. Этот запрос всегда совпадает с
     *                {@link WebService#getRequest()}
     */
    void render(Object data, Request request) throws Exception;

}
