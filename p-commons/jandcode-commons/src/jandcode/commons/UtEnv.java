package jandcode.commons;

import jandcode.commons.env.*;

/**
 * Утилиты для среды
 */
public class UtEnv {

    /**
     * Загрузить среду из указанного файла
     *
     * @param fileName файл с описанием среды. Обычно '.env'.
     * @param test     соотвествует значению {@link Env#isTest()}, которое желаете получить.
     * @return среда
     */
    public static Env loadEnv(String fileName, boolean test) {
        return new EnvLoader().load(fileName, test);
    }


}
