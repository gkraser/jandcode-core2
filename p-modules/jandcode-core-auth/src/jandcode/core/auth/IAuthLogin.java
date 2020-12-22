package jandcode.core.auth;

/**
 * Интерфейс для аутентификации
 */
public interface IAuthLogin {

    /**
     * Аутентифицировать пользователя
     *
     * @param authToken токен аутентификации
     * @return пользователь или ошибка, если невозможно аутентифицировать пользователя.
     */
    AuthUser login(AuthToken authToken) throws Exception;

}
