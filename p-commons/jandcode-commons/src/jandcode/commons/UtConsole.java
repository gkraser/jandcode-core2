package jandcode.commons;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;

/**
 * Утилиты для консоли.
 * Управление кодировкой консоли. Актуально для русского языка, как минимум.
 */
public class UtConsole {

    /**
     * Системное свойство с кодировкой консоли
     */
    public static final String SYSPROPERTY_CONSOLECHARSET = "jandcode.consolecharset"; //NON-NLS

    private static String _consoleCharset = null;

    /**
     * Инициализировать консоль по свойству SYSPROPERTY_CONSOLECHARSET
     * либо по реальной кодировке консоли.
     */
    public static void setupConsoleCharset() {
        String cc = System.getProperty(SYSPROPERTY_CONSOLECHARSET);
        if (cc != null && cc.length() > 0) {
            if ("auto".equals(cc)) { //NON-NLS
                cc = detectConsoleCharset();
                if (cc == null) {
                    return;
                }
            }
            try {
                System.setOut(new PrintStream(System.out, true, cc));
                System.setErr(new PrintStream(System.err, true, cc));
                _consoleCharset = cc;
            } catch (UnsupportedEncodingException e) {
            }
        }
    }

    /**
     * Текущая кодировка консоли
     */
    public static String getConsoleCharset() {
        //
        try {
            final Class<? extends PrintStream> stdOutClass = System.out.getClass();
            final Field charOutField = stdOutClass.getDeclaredField("charOut");
            charOutField.setAccessible(true);
            OutputStreamWriter o = (OutputStreamWriter) charOutField.get(System.out);
            return o.getEncoding();
        } catch (Exception e) {
        }
        //
        if (_consoleCharset == null) {
            String cs = detectConsoleCharset();
            if (cs != null) {
                return cs;
            }
            return Charset.defaultCharset().toString();
        }
        return _consoleCharset;
    }

    /**
     * Выявляет текущую кодировку консоли
     */
    public static String detectConsoleCharset() {
        // reflection!
        Constructor[] ctors = Console.class.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            ctor.setAccessible(true);
            Console c = (Console) ctor.newInstance();
            Field f = c.getClass().getDeclaredField("cs");
            f.setAccessible(true);
            return f.get(c).toString();
        } catch (Exception e) {
        }
        return null;
    }

}
