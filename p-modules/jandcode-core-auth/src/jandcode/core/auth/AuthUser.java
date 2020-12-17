package jandcode.core.auth;

import jandcode.commons.variant.*;

/**
 * Информация о пользователе
 */
public interface AuthUser {

    /**
     * Признак гостя, т.е. пользователя, который не
     * аутентифицирован.
     */
    boolean isGuest();

    /**
     * Атрибуты пользователя.
     * Только для чтения.
     */
    IVariantMap getAttrs();

}
