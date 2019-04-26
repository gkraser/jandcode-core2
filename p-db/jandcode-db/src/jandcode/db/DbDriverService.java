package jandcode.db;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Драйвера базы данных
 */
public interface DbDriverService extends Comp {

    /**
     * Зарегистрированные драйвера
     */
    NamedList<DbDriverDef> getDbDrivers();

}
