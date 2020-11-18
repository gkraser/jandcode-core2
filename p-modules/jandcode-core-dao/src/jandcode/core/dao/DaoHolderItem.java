package jandcode.core.dao;

import jandcode.commons.named.*;

/**
 * Элемент хранилища dao.
 * Представляет собой описание метода dao
 * привязанное к имени, которое выглядет как путь (a/b/c)
 */
public interface DaoHolderItem extends INamed {

    /**
     * Метод
     */
    DaoMethodDef getMethodDef();

    /**
     * Какой {@link DaoInvoker} используется для выполнения.
     * Определяется настройками {@link DaoHolder}, которому принадлежит элемент.
     */
    String getDaoInvokerName();

}
