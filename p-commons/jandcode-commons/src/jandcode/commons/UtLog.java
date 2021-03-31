package jandcode.commons;

import jandcode.commons.log.*;
import org.slf4j.*;

/**
 * Утилиты логирования через logback
 */
public class UtLog {

    private static LogbackConfigurator logbackConfigurator = new LogbackConfigurator();

    //////

    /**
     * Имя логгера logback ('jc.console'), через который выводятся информационные сообщения
     * на консоль.
     */
    public static final String LOG_CONSOLE = "jc.console";

    /**
     * Отключение логгирования
     */
    public static void logOff() {
        logbackConfigurator.applyConfig(logbackConfigurator.getLogOffConfig());
    }

    /**
     * Включение логгирования по умолчанию
     */
    public static void logOn() {
        logbackConfigurator.applyConfig(logbackConfigurator.getLogOnConfig());
    }

    /**
     * Включение логгирования с указанной конфигурацией
     */
    public static void logOn(String cfg) {
        if (UtString.empty(cfg)) {
            cfg = logbackConfigurator.getLogOnConfig();
        }
        logbackConfigurator.applyConfig(cfg);
    }

    //////

    /**
     * Возвращает logger, который, по соглашению, используется для вывода информационных
     * сообщений на консоль.
     */
    public static Logger getLogConsole() {
        return LoggerFactory.getLogger(LOG_CONSOLE);
    }

    //////

    /**
     * Создать {@link AppLogManager} для указанного каталога приложения.
     * С его помощью можно включать логирование с загрузкой конфигурации из указанного
     * каталога.
     *
     * @param appDir каталог приложения, откуда будут братся конфиги для логирования
     */
    public static AppLogManager createAppLogManager(String appDir) {
        return new AppLogManager(appDir);
    }

}
