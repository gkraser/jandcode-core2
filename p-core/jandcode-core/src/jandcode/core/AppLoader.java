package jandcode.core;

import jandcode.core.impl.*;

/**
 * Загрузчик приложения
 */
public class AppLoader {

    /**
     * Загрузка приложения из указанного файла
     */
    public static App load(String appConfFile) throws Exception {
        return new AppImpl(appConfFile, false);
    }

}
