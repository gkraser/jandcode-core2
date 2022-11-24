package jandcode.core.dbm.mdb;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.sql.*;
import jandcode.core.store.*;

/**
 * Утилиты для модели и ее базы данных.
 * Основное средство работы с базой данных для приложений.
 * Объект притворяется базой данных {@link Db}.
 */
public interface Mdb extends IAppLink, IModelLink, Db,
        IDaoInvoker, IDomainService, IDictService, IQueryRecord, IStoreService,
        ISqlService, IOutData, IMdbGenId, IMdbRec, IValidateErrorsLink, ValidateErrors {

    /**
     * Создать экземпляр класса с привязкой к модели.
     * Если класс реализует интерфейс {@link IMdbLinkSet}, то ему будет присвоена
     * ссылка на mdb.
     * <p>
     * Используется для создания потомков {@link BaseMdbUtils}.
     */
    <A> A create(Class<A> cls);

}
