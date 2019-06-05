package jandcode.commons;

import jandcode.commons.ansifer4.*;
import jandcode.commons.ansifer4.impl.*;

/**
 * ansi цвета в консоле.
 */
public class UtAnsifer {

    private static Ansifer _inst = new AnsiferImpl();

    /**
     * Ссылка на глобальный экземпляр {@link Ansifer}
     */
    public static Ansifer getAnsifer() {
        return _inst;
    }

    //////

    /**
     * @see Ansifer#color(java.lang.String, java.lang.String)
     */
    public static String color(String styleOrColor, String s) {
        return getAnsifer().color(styleOrColor, s);
    }

    /**
     * Обрамление строки ansi-кодами для указанных цветов.
     * Если ansi не инициализирована - преобразования не производится.
     *
     * @param color      каким цветом
     * @param background каким фоном
     * @param s          какую строку
     */
    public static String color(String color, String background, String s) {
        return getAnsifer().color(getAnsifer().getStyle(color, background), s);
    }

}
