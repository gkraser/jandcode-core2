package jandcode.commons;

import java.nio.charset.*;

/**
 * Утилиты для консоли.
 */
public class UtConsole {

    /**
     * Текущая кодировка консоли.
     * Если сейчас вывод перенаправлен в файл, возвращает правильную кожировку.
     */
    public static String getConsoleCharset() {
        String enc = System.getProperty("sun.stdout.encoding");
        if (UtString.empty(enc)) {
            enc = Charset.defaultCharset().name();
        }
        return enc;
    }

}
