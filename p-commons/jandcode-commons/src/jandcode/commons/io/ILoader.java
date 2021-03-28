package jandcode.commons.io;

import java.io.*;

/**
 * Объект, который может читать себя из Reader
 */
public interface ILoader {

    /**
     * Прочитать.
     *
     * @param reader откуда
     */
    void loadFrom(Reader reader) throws Exception;

    /**
     * Создание загрузчика для загрузки из этого ILoader
     */
    default LoadFrom load() {
        return new LoadFrom(this);
    }

}
