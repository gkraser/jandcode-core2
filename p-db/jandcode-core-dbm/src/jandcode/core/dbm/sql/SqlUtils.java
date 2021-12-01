package jandcode.core.dbm.sql;

import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.impl.*;

/**
 * Утилиты для текста sql
 */
public interface SqlUtils extends ISqlPaginate {

    /**
     * Создать экземпляр {@link SqlUtils}
     *
     * @param model для какой модели
     */
    static SqlUtils create(Model model) {
        return new SqlUtilsImpl(model);
    }

}
