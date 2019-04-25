package jandcode.commons.outtable;


import jandcode.commons.io.*;

/**
 * Объект для вывода табличных данных на консоль для просмотра во время отладки.
 */
public interface OutTableSaver extends ISaver {

    /**
     * Строка для вывода null-значений.
     */
    String NULL_STRING_VALUE = "<null>";

    /**
     * Сколько записей выводить. По умолчанию - все
     */
    void setLimit(int limit);

}
