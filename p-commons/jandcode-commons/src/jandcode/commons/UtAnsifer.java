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
     * Обрамление строки ansi-кодами для указанного стиля.
     * Если ansi не инициализирована - преобразования не производится.
     *
     * @param styleOrColor каким стилем или цветом. Если имеется зарегистрированный стиль
     *                     с таким именем, то используется он. Иначе -
     *                     интерпретируется как цвет.
     * @param s            какую строку
     */
    public static String color(String styleOrColor, String s) {
        return getAnsifer().color(getAnsifer().getStyle(styleOrColor), s);
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
