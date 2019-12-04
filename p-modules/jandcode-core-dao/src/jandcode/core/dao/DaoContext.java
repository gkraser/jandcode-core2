package jandcode.core.dao;

import jandcode.core.*;

/**
 * Контекст исполнения dao
 */
public interface DaoContext extends IAppLink, BeanFactoryOwner, IBeanIniter {

    /**
     * Время начала выполнения dao.
     */
    long getStartTime();

}
