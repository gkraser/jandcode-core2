package jandcode.core;

import jandcode.commons.*;
import jandcode.commons.env.*;
import jandcode.core.impl.*;

/**
 * Загрузчик приложения
 */
public class AppLoader {

    /**
     * Загрузка приложения.
     *
     * @param appConfFile файл с конфигурацией приложения. Может в принципе быть
     *                    ресурсом в jar
     * @param test        признак тестовой среды. Если приложение создается в тестах,
     *                    нужно передать true, иначе (в подавляющем большинстве случаев - false)
     * @param appdir      каталог с приложением. Каталог в котором приложение установлено.
     *                    Если передать null - определится автоматически.
     *                    см: {@link UtEnv#resolveAppdir(java.lang.String)}
     * @param env         среда, если передать null - загрузится автоматически.
     *                    см: {@link UtEnv#loadEnv(java.lang.String, boolean)}
     */
    public static App load(String appConfFile, String appdir, Env env, boolean test) throws Exception {
        return new AppImpl(appConfFile, appdir, env, test);
    }

    /**
     * Загрузка приложения из указанного файла
     */
    public static App load(String appConfFile) throws Exception {
        return load(appConfFile, null, null, false);
    }

    /**
     * Загрузка приложения из указанного файла с указанием признака тестовой среды
     */
    public static App load(String appConfFile, boolean test) throws Exception {
        return load(appConfFile, null, null, test);
    }

}
