package jandcode.commons.ansifer;

import java.util.*;

/**
 * ansi в консоле
 */
public interface Ansifer {

    /**
     * Включена ли поддержка ansi консоли.
     */
    boolean isOn();

    /**
     * Включить поддержку ansi консоли.
     * Если этот метод не вызвать, консоль будет обычной.
     * Вызовы ansiOn/ansiOff должны быть сбалансированы.
     */
    void ansiOn();

    /**
     * Выключить поддержку ansi консоли.
     * Если этот метод вызвать, консоль будет обычной.
     * Вызовы ansiOn/ansiOff должны быть сбалансированы.
     */
    void ansiOff();


    ////// styles

    /**
     * Регистрация стиля
     *
     * @param styleName    имя стиля
     * @param color        цвет
     * @param background   фон
     * @param defaultStyle стиль по умолчанию. Если true и такой стиль уже определен,
     *                     то вызов игнорируется.
     */
    void registerStyle(String styleName, AnsiferColor color, AnsiferColor background, boolean defaultStyle);

    /**
     * Получить стиль по имени. Если такого стиля нет - возвращается null.
     *
     * @param styleName имя стиля или имя цвета.
     */
    AnsiferStyle getStyle(String styleName);

    /**
     * Получить стиль по цвету и фону.
     */
    AnsiferStyle getStyle(String color, String background);

    /**
     * Имена зарегистрированных стилей
     */
    Collection<String> getStyleNames();


    ////// 

    /**
     * Обрамление строки ansi-кодами для указанного стиля.
     * Если ansi не инициализирована - преобразования не производится.
     *
     * @param style каким стилем. Может быть null, тогда игнорируется.
     * @param s     какую строку
     */
    String color(AnsiferStyle style, String s);

    /**
     * Обрамление строки ansi-кодами для указанного стиля.
     * Если ansi не инициализирована - преобразования не производится.
     *
     * @param styleOrColor каким стилем или цветом. Если имеется зарегистрированный стиль
     *                     с таким именем, то используется он. Иначе -
     *                     интерпретируется как цвет.
     * @param s            какую строку
     */
    String color(String styleOrColor, String s);


}
