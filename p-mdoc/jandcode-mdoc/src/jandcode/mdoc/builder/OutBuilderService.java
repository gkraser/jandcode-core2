package jandcode.mdoc.builder;

import jandcode.core.*;
import jandcode.mdoc.*;

/**
 * Сервис для builder
 */
public interface OutBuilderService extends Comp {

    /**
     * Создать builder
     *
     * @param name имя зарегистрированного builder
     * @param doc  для какого документа
     */
    OutBuilder createBuilder(String name, Doc doc) throws Exception;

}
