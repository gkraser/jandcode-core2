package jandcode.commons.io;

import java.io.*;

/**
 * Объект, который может записывать себя во Writer
 */
public interface ISaver {

    /**
     * Записать.
     *
     * @param writer куда
     */
    void saveTo(Writer writer) throws Exception;

    /**
     * Создание записывальщика этого объекта ISaver
     */
    SaveTo save();

}
