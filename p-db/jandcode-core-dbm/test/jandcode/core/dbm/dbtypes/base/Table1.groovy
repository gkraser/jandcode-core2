package jandcode.core.dbm.dbtypes.base

import jandcode.commons.reflect.*

/**
 * Структура простой таблицы
 */
class Table1 {
    long id

    @FieldProps(size = 20)
    String text
}
