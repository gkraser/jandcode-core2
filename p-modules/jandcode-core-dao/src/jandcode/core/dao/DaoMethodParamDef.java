package jandcode.core.dao;

import jandcode.commons.named.*;

import java.lang.reflect.*;

/**
 * Описание параметра метода dao
 */
public interface DaoMethodParamDef extends INamed {

    /**
     * Какому методу принадлежит
     */
    Method getMethod();

    /**
     * Тип параметра
     */
    Class<?> getType();

    /**
     * Порядковый номер параметра
     */
    int getIndex();

}
