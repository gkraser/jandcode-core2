package jandcode.commons.ansifer.impl;


import jandcode.commons.ansifer.*;

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
