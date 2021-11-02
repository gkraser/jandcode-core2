package jandcode.core.dao;

import jandcode.commons.named.*;

import java.lang.reflect.*;

/**
 * Описание java-метода,
 * Имя - имя метода.
 */
public interface DaoMethodDef extends INamed {

    /**
     * Какому классу принадлежит
     */
    Class getCls();

    /**
     * Ссылка на реальный java-метод
     */
    Method getMethod();

    /**
     * Параметры метода
     */
    NamedList<DaoMethodParamDef> getParams();

}
