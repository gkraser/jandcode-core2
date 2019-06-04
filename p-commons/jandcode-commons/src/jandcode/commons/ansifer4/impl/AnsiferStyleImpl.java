package jandcode.commons.ansifer4.impl;


import jandcode.commons.ansifer4.*;

/**
 * ansi стиль
 */
public class AnsiferStyleImpl implements AnsiferStyle {

    private AnsiferColor color;
    private AnsiferColor background;

    public AnsiferStyleImpl(AnsiferColor color, AnsiferColor background) {
        this.color = color;
        this.background = background;
    }

    /**
     * Цвет
     */
    public AnsiferColor getColor() {
        return color;
    }

    /**
     * Фон
     */
    public AnsiferColor getBackground() {
        return background;
    }

}
