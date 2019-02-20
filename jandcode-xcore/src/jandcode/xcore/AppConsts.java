package jandcode.xcore;

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
     * Каталог приложения. Определяется переменной PROP_APP_DIR.
     * Если она не определена, возвращается текущий рабочий каталог.
     */
    public static String getAppdir() {
        String appdir = System.getProperty(PROP_APP_DIR);
        if (UtString.empty(appdir)) {
            appdir = UtFile.getWorkdir();
        }
        return UtFile.abs(appdir);
    }

}
