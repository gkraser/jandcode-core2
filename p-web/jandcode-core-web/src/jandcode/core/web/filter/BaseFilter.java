package jandcode.core.web.filter;

import jandcode.core.*;
import jandcode.core.web.*;

/**
 * Базовый предок для filter
 */
public abstract class BaseFilter extends BaseComp implements IFilter {

    /**
     * Ссылка на {@link WebService}
     */
    public WebService getWebService() {
        return getApp().bean(WebService.class);
    }


}
