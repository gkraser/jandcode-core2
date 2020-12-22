package jandcode.core.auth;

import jandcode.core.*;

/**
 * Сервис аутентификации и авторизации
 */
public interface AuthService extends Comp, IAuthLogin {

    /**
     * Текущий пользователь.
     * Всегда возвращает не null.
     */
    AuthUser getCurrentUser();

    /**
     * Установить текущего пользователя.
     *
     * @param user пользователь. Можно передать null, что означает,
     *             что пользователь - гость.
     */
    void setCurrentUser(AuthUser user);

    /**
     * Используемый аутентификатор
     */
    AuthProcessor getAuthProcessor();

}
