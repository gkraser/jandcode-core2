package jandcode.commons.ansifer.impl;

import jandcode.commons.ansifer.*;

/**
 * Описание стиля
 */
public class AnsiferStyle {

    private AnsiferColor color;
    private AnsiferColor background;

    public AnsiferStyle(AnsiferColor color, AnsiferColor background) {
        this.color = color;
        this.background = background;
    }

    public AnsiferStyle(AnsiferColor color) {
        this.color = color;
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
