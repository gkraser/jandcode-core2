package jandcode.core;

import jandcode.commons.*;

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
     * Файл, описывающий среду.
     * Он же используется как маркер корня приложения,
     * если явно не задана {@link AppConsts#PROP_APP_DIR}
     */
    public static final String FILE_ENV = ".env";

    /**
     * Выяснить каталог приложения.
     * Определяется переменной {@link AppConsts#PROP_APP_DIR}.
     * <p>
     * Если она не определена, ищется каталог, который содержит {@link AppConsts#FILE_ENV},
     * начиная с defaultAppDir и вверх по иерархии каталогов.
     * <p>
     * Если она не определена, принимается defaultAppDir.
     *
     * @param defaultAppDir каталог по умолчанию, если не удалось определить.
     *                      Если null, то принимается как workDir
     */
    public static String resolveAppdir(String defaultAppDir) {
        String appdir = System.getProperty(PROP_APP_DIR);
        if (!UtString.empty(appdir)) {
            return UtFile.abs(appdir);
        }

        if (defaultAppDir == null) {
            defaultAppDir = UtFile.getWorkdir();
        }

        if (UtString.empty(appdir)) {
            String f = UtFile.findFileUp(FILE_ENV, defaultAppDir);
            if (f != null) {
                appdir = UtFile.path(f);
            }
        }

        if (UtString.empty(appdir)) {
            appdir = defaultAppDir;
        }

        return UtFile.abs(appdir);
    }

}
