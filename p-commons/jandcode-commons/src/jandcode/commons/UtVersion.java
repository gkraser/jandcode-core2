package jandcode.commons;

import jandcode.commons.version.*;

/**
 * Утилиты для версий
 */
public class UtVersion {

    /**
     * Версия указанного пакета
     */
    public static String getVersion(String pak) {
        return new VersionInfo(pak).getVersion();
    }

    /**
     * Версия пакета указанного класса
     */
    public static String getVersion(Class cls) {
        return new VersionInfo(cls.getPackage().getName()).getVersion();
    }

}
