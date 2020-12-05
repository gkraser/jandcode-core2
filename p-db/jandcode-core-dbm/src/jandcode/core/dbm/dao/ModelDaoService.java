package jandcode.core.dbm.dao;

import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.*;

/**
 * Сервис dao для модели
 */
public interface ModelDaoService extends Comp, IModelMember {

    /**
     * DaoInvoker для модели
     */
    DaoInvoker getDaoInvoker();

    /**
     * Личный DaoHolder для модели
     */
    DaoHolder getDaoHolder();

}
