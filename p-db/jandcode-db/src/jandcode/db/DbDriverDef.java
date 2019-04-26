package jandcode.db;

import jandcode.core.*;

/**
 * Объявление dbdriver
 */
public interface DbDriverDef extends Comp {

    /**
     * Создать экземпляр
     */
    DbDriver createInst();

    /**
     * Получить кешированный экземпляр
     */
    DbDriver getInst();

}
