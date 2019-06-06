package jandcode.commons;

import jandcode.commons.ansifer.*;
import jandcode.commons.ansifer.impl.*;

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
     * @see Ansifer#ansiOn()
     */
    public static void ansiOn() {
        getAnsifer().ansiOn();
    }

    /**
     * @see Ansifer#ansiOff()
     */
    public static void ansiOff() {
        getAnsifer().ansiOff();
    }

    //////

    /**
     * @see Ansifer#registerStyle(java.lang.String, jandcode.commons.ansifer.AnsiferColor, jandcode.commons.ansifer.AnsiferColor, boolean)
     */
    public static void registerStyle(String styleName, AnsiferColor color, AnsiferColor background, boolean defaultStyle) {
        getAnsifer().registerStyle(styleName, color, background, defaultStyle);
    }

    /**
     * см {@link Ansifer#registerStyle(java.lang.String, jandcode.commons.ansifer.AnsiferColor, jandcode.commons.ansifer.AnsiferColor, boolean)},
     * где defaultStyle=false
     */
    public static void registerStyle(String styleName, AnsiferColor color, AnsiferColor background) {
        getAnsifer().registerStyle(styleName, color, background, false);
    }

    //////

    /**
     * @see Ansifer#color(java.lang.String, java.lang.String)
     */
    public static String color(String styleOrColor, String s) {
        return getAnsifer().color(styleOrColor, s);
    }

    /**
     * @see Ansifer#color(java.lang.String, java.lang.String)
     */
    public static String color(AnsiferStyle style, String s) {
        return getAnsifer().color(style, s);
    }

    /**
     * @see Ansifer#color(java.lang.String, java.lang.String, java.lang.String)
     */
    public static String color(String color, String background, String s) {
        return getAnsifer().color(color, background, s);
    }

    /**
     * @see Ansifer#color(jandcode.commons.ansifer.AnsiferColor, jandcode.commons.ansifer.AnsiferColor, java.lang.String)
     */
    public static String color(AnsiferColor color, AnsiferColor background, String s) {
        return getAnsifer().color(color, background, s);
    }

}
