package jandcode.core.web.render.impl;

import jandcode.core.web.render.*;

/**
 * Рендер строкового представления объекта.
 */
public class ToStringRender extends BaseRender {

    protected void onRender(Object data) throws Exception {
        getRequest().getOutWriter().write(data.toString());
    }

}
