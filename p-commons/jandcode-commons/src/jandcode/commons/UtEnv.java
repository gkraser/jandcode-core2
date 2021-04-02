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

    /**
     * Выяснить каталог приложения.
     * Определяется переменной {@link UtilsConsts#PROP_APP_DIR}.
     * <p>
     * Если она не определена, ищется каталог, который содержит {@link UtilsConsts#FILE_ENV},
     * начиная с defaultAppDir и вверх по иерархии каталогов.
     * <p>
     * Если она не определена, принимается defaultAppDir.
     *
     * @param defaultAppDir каталог по умолчанию, если не удалось определить.
     *                      Если null, то принимается как workDir
     */
    public static String resolveAppdir(String defaultAppDir) {
        String appdir = System.getProperty(UtilsConsts.PROP_APP_DIR);
        if (!UtString.empty(appdir)) {
            return UtFile.abs(appdir);
        }

        if (defaultAppDir == null) {
            defaultAppDir = UtFile.getWorkdir();
        }

        if (UtString.empty(appdir)) {
            String f = UtFile.findFileUp(UtilsConsts.FILE_ENV, defaultAppDir);
            if (f != null) {
                appdir = UtFile.path(f);
            }
        }

        if (UtString.empty(appdir)) {
            appdir = defaultAppDir;
        }

        return UtFile.abs(appdir);
    }

    /**
     * Выяснить main-класс, который запустил приложение.
     */
    public static String resolveMainClass() {
        StackTraceElement[] cause = Thread.currentThread().getStackTrace();
        return cause[cause.length - 1].getClassName();
    }

    /**
     * Выяснить команду, которая запустила приложение.
     * Определяется переменной {@link UtilsConsts#PROP_APP_CMDNAME}.
     * <p>
     * Если она не определена, возвращается main-class, который запустил приложение.
     */
    public static String resolveCmdName() {
        String cmdName = System.getProperty(UtilsConsts.PROP_APP_CMDNAME);
        if (!UtString.empty(cmdName)) {
            return cmdName;
        }
        return resolveMainClass();
    }

}
