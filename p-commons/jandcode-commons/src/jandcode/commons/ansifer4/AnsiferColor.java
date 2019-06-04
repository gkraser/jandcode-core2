package jandcode.commons.ansifer4;

/**
 * ansi цвет
 */
public enum AnsiferColor {

    // обычные

    BLACK(0, false),
    RED(1, false),
    GREEN(2, false),
    YELLOW(3, false),
    BLUE(4, false),
    MAGENTA(5, false),
    CYAN(6, false),
    WHITE(7, false),

    // яркие

    BLACK_H(0, true),
    RED_H(1, true),
    GREEN_H(2, true),
    YELLOW_H(3, true),
    BLUE_H(4, true),
    MAGENTA_H(5, true),
    CYAN_H(6, true),
    WHITE_H(7, true);

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
            return valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
