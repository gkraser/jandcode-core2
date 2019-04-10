package jandcode.web.render;

import jandcode.web.*;

/**
 * Фабрика для WebRender
 */
public interface IRenderFactory {

    /**
     * Создать render для данных.
     *
     * @param request запрос
     * @return null, если нельзя создать render для указанных данных
     */
    IRender createRender(Object data, Request request);


}
