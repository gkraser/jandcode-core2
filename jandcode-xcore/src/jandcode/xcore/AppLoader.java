package jandcode.xcore;

import jandcode.xcore.impl.*;

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
