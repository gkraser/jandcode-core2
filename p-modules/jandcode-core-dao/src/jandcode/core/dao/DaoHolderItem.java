package jandcode.core.dao;

import jandcode.commons.named.*;

/**
 * Элемент хранилища dao.
 * Представляет собой описание метода dao.
 */
public interface DaoHolderItem extends INamed {

    /**
     * Метод
     */
    DaoMethodDef getMethodDef();

    /**
     * Какой {@link DaoInvoker} используется для выполнения.
     */
    String getDaoInvokerName();

}
