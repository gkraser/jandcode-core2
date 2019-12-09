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
     * Какой {@link DaoManager} используется для выполнения.
     */
    String getDaoManagerName();

}
