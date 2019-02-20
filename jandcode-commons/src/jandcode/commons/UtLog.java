package jandcode.commons;

import jandcode.commons.log.*;

/**
 * Утилиты логирования через logback
 */
public class UtLog {

    private static LogbackConfigurator logbackConfigurator = new LogbackConfigurator();

    //////

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

}
