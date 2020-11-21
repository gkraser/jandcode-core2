package jandcode.core.dbm.mdb;

import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;

/**
 * Утилиты для модели и ее базы данных.
 * Основное средство работы с базой данных для приложений.
 * Объект притворяется базой данных {@link Db}.
 */
public interface Mdb extends IAppLink, IModelLink, Db,
        IDaoInvoker, IDomainService, IDictService {

}
