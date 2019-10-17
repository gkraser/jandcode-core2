package jandcode.core.web.action;

import jandcode.core.*;

/**
 * Объявление action.
 * Имя объекта - имя action.
 */
public interface ActionDef extends Comp {

    /**
     * Создать экземпляр action.
     */
    IAction createInst();

    /**
     * Сколько фасетов, разделенных '/', имеется в имени action.
     */
    int getCountFacet();

}
