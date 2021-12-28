package jandcode.core.dbm.dbstruct;

import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

/**
 * Представление поля домена, как столбца таблицы в базе данных
 */
public interface FieldDb extends Comp, IModelMember, IFieldMember {

    /**
     * Тип в базе данных
     */
    DbDataType getDbDatatype();

    /**
     * sql тип поля в виде строки
     */
    String getSqlType();

    /**
     * Если поле ссылка, то она каскадная.
     * Это означает, что при удалении записи, на которую идет ссылка,
     * должна быть удалена и запись, откуда идет ссылка.
     * <p>
     * conf: {@code <field db.refcascade="bool"/>}
     */
    boolean isRefCascade();

}
