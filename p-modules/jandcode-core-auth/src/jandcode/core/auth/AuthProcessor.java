package jandcode.core.auth;

/**
 * Интерфейс для аутентификаторов.
 */
public interface AuthProcessor extends IAuthLogin {

    /**
     * Возвращает true, если может использовать указанный токен для аутентификации.
     */
    boolean isSupportedAuthToken(AuthToken authToken);

}
