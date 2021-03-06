package jandcode.jc;

import groovy.ant.*;

/**
 * Доступ к кешу AntBuilder
 */
public interface IAnts {

    /**
     * Возвращает кешированный экземпляр AntBuilder для указанного базового каталога
     */
    AntBuilder getAnt(String basedir);

    /**
     * Возвращает кешированный экземпляр AntBuilder для указанного базового каталога
     */
    AntBuilder getAnt(Dir dir);

}
