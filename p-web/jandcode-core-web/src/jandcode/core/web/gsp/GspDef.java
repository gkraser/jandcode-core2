package jandcode.core.web.gsp;

import jandcode.core.*;

/**
 * Объявление gsp
 */
public interface GspDef extends Comp {

    /**
     * Создать экземпляр gsp.
     */
    Gsp createInst();

    /**
     * Возвращает путь до объекта, которым представлен шаблон.
     * Если путь не определен, возвращается пустая строка.
     */
    String getGspPath();

    /**
     * Возвращает исходный текст gsp, если доступен.
     * Если недоступен - пустую строку.
     */
    String getGspSourceText();

    /**
     * Возвращает исходный текст groovy-класса gsp, если доступен.
     * Если недоступен - пустую строку.
     */
    String getGspClassText();


}
