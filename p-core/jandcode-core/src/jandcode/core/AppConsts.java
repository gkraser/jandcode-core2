package jandcode.core;

import jandcode.commons.*;
import jandcode.commons.moduledef.*;

/**
 * Константы
 */
public class AppConsts {

    /**
     * Имя файла, откуда загружается приложение
     */
    public static final String FILE_APP_CONF = "app.cfx";

    /**
     * Имя модуля, который представляет собой файл приложения app.cfx.
     */
    public static final String MODULE_APP = "app";

    /**
     * Системное свойство - каталог установленного приложения
     */
    public static final String PROP_APP_DIR = "jandcode.app.appdir";

    /**
     * conf путь, в котором описаны обработчики {@link AppConfHandler}.
     * Используется в module.cfx, app.cfx или test.cfx
     */
    public static final String APP_CONF_HANDLER = "app-conf-handler";


    /**
     * Выяснить каталог приложения.
     * Определяется переменной PROP_APP_DIR.
     * Если она не определена, ищется pathprop {@link ModuleDefConsts#PROP_PROJECT_ROOT}.
     * Если она не определена, defaultAppDir.
     *
     * @param defaultAppDir каталог по умолчанию, если не удалось определить.
     *                      Если null, то принимается как workDir
     */
    public static String resolveAppdir(String defaultAppDir) {
        if (defaultAppDir == null) {
            defaultAppDir = UtFile.getWorkdir();
        }

        String appdir = System.getProperty(PROP_APP_DIR);

        if (UtString.empty(appdir)) {
            appdir = UtFile.getPathprop(ModuleDefConsts.PROP_PROJECT_ROOT, defaultAppDir);
        }

        if (UtString.empty(appdir)) {
            appdir = defaultAppDir;
        }
        return UtFile.abs(appdir);
    }

}
