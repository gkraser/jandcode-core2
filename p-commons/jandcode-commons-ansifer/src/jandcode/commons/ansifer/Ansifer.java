package jandcode.commons.ansifer;

import jandcode.commons.ansifer.impl.*;

import java.util.*;

/**
 * ansi-краски в консоле
 */
public class Ansifer {

    private static Ansifer _inst = new Ansifer();

    /**
     * Глобальный экземпляр
     */
    public static Ansifer getInst() {
        return _inst;
    }

    //////

    private HashMap<String, AnsiferStyle> styles = new HashMap<>();
    private boolean installed;
    private AnsiferProvider provider;
    private boolean enabled = true;

    /**
     * Инсталлирован ли ansi
     */
    public boolean isInstalled() {
        return installed;
    }

    /**
     * Инициализация ansi консоли.
     * Если этот метод не вызвать, консоль будет обычной.
     */
    public void install() {
        if (installed) {
            return;
        }

        //
        provider = new AnsiferProviderDefault();

        try {
            if (provider.install()) {
                installed = true;
            }
        } catch (Throwable e) {
            provider = null;
            // ignore errors
        }
    }

    /**
     * Деинициализация ansi консоли.
     * Если этот метод вызвать, консоль будет обычной.
     */
    public void uninstall() {
        if (!installed || provider == null) {
            return;
        }

        //
        try {
            if (provider.uninstall()) {
                installed = false;
                provider = null;
            }
        } catch (Exception e) {
            // ignore errors
        }
    }


    /**
     * Регистрация стиля
     *
     * @param styleName  имя стиля
     * @param color      цвет
     * @param background фон
     */
    public void registerStyle(String styleName, AnsiferColor color, AnsiferColor background) {
        styles.put(styleName, new AnsiferStyle(color, background));
    }

    /**
     * Регистрация стиля
     *
     * @param styleName имя стиля
     * @param color     цвет
     */
    public void registerStyle(String styleName, AnsiferColor color) {
        styles.put(styleName, new AnsiferStyle(color, null));
    }

    /**
     * Обрамление строки ansi-кодами для указанного стиля.
     * Если ansi не инициализирована - преобразования не производится.
     *
     * @param styleName каким стилем
     * @param s         какую строку
     */
    public String style(String styleName, String s) {
        if (s == null) {
            s = "";
        }
        if (!installed || provider == null || !enabled) {
            return s;
        }
        AnsiferStyle st = styles.get(styleName);
        if (st == null) {
            return s;
        }

        return provider.style(st, s);
    }

    /**
     * Обрамление строки ansi-кодами для указанных цветов.
     * Если ansi не инициализирована - преобразования не производится.
     *
     * @param color      каким цветом
     * @param background каким фоном
     * @param s          какую строку
     */
    public String color(AnsiferColor color, AnsiferColor background, String s) {
        if (s == null) {
            s = "";
        }
        if (!installed || provider == null || !enabled) {
            return s;
        }
        AnsiferStyle st = new AnsiferStyle(color, background);

        return provider.style(st, s);
    }

    /**
     * Разрешен ли. Можно временно запрещать.
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
