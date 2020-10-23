package jandcode.core.web;

import jandcode.core.*;

/**
 * Контекст запроса.
 */
public interface RequestContext extends IAppLink, BeanFactoryOwner, IBeanIniter {

    /**
     * Ссылка на запрос
     */
    Request getRequest();

}
