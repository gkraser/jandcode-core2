package jandcode.core.apx.dbm.sqlfilter;

import jandcode.core.*;
import jandcode.core.dbm.*;

import java.util.*;

public interface SqlFilterService extends Comp, IModelMember {

    /**
     * Зарегистрированные имена для {@link SqlFilterBuilder}
     */
    List<String> getSqlFilterBuilderNames();

    /**
     * Создать экземпляр {@link SqlFilterBuilder} по зарегистрированному имени
     */
    SqlFilterBuilder createSqlFilterBuilder(String name);

}
