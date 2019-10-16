package jandcode.core.web.virtfile.impl;

/**
 * Проверка расширения файла на принадлежность к шаблонам
 */
public interface ITmlCheck {

    /**
     * Является ли расширение ext расширением шаблона
     */
    boolean isTml(String ext);

}
