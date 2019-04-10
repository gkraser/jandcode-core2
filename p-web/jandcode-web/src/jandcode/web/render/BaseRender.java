package jandcode.web.render;

import jandcode.core.*;
import jandcode.web.*;

/**
 * Базовый предок для render
 */
public abstract class BaseRender extends BaseComp implements IRender {

    private Request request;

    /**
     * Реализация {@link IRender}. Этот метод не нужно перекрывать.
     */
    public void render(Object data, Request request) throws Exception {
        this.request = request;
        onRender(data);
    }

    /**
     * Реализация render
     *
     * @param data объект с данными
     */
    protected abstract void onRender(Object data) throws Exception;

    /**
     * Ссылка на {@link WebService}
     */
    public WebService getWebService() {
        return getApp().bean(WebService.class);
    }

    /**
     * Запрос
     */
    public Request getRequest() {
        return request;
    }
}
