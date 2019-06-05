package jandcode.commons.ansifer;

import jandcode.commons.*;

/**
 * ansi цвет
 */
public enum AnsiferColor {

    // обычные

    black(0, false),
    red(1, false),
    green(2, false),
    yellow(3, false),
    blue(4, false),
    magenta(5, false),
    cyan(6, false),
    white(7, false),

    // яркие

    black_h(0, true),
    red_h(1, true),
    green_h(2, true),
    yellow_h(3, true),
    blue_h(4, true),
    magenta_h(5, true),
    cyan_h(6, true),
    white_h(7, true);

    ///

    private int code;
    private boolean bright;

    AnsiferColor(int code, boolean bright) {
        this.code = code;
        this.bright = bright;
    }

    /**
     * Код цвета (0-7)
     */
    public int code() {
        return code;
    }

    /**
     * Яркий
     */
    public boolean bright() {
        return bright;
    }

    /**
     * Конвертация из строки. Возвращает null, если цвет неизвестный.
     */
    public static AnsiferColor fromString(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        try {
            return valueOf(UtStrDedup.lower(s));
        } catch (Exception e) {
            return null;
        }
    }

}
