package jandcode.web.render;

import jandcode.core.*;

/**
 * Объявление render
 */
public interface RenderDef extends Comp {

    /**
     * Создать экземпляр render.
     */
    IRender createInst();

}
