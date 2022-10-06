package jandcode.core.apx.dbm.sqlfilter;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

public interface SqlFilterService extends Comp, IModelMember {

    /**
     * Зарегистрированные {@link SqlFilterWhereDef}
     */
    NamedList<SqlFilterWhereDef> getSqlFilterWhereDefs();

    /**
     * Создать экземпляр {@link SqlFilterWhere} по зарегистрированному имени
     */
    SqlFilterWhere createSqlFilterWhere(String name);

}
