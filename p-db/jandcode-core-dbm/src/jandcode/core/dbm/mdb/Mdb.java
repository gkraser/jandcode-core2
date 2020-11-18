package jandcode.core.dbm.mdb;

import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;

/**
 * Утилиты для модели и ее базы данных.
 * Основное средство работы с базой данных для приложений.
 * Объект притворяется базой данных {@link Db}.
 * Все методы расчитывают на то, что будет подключена модель 'dbm.base',
 * иначе некоторые методы могут вызывать ошибку "bean не найден".
 */
public interface Mdb extends IAppLink, IModelLink, Db {

}
