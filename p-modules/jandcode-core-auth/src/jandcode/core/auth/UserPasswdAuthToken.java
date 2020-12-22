package jandcode.core.auth;

/**
 * Токен авторизации для имени пользователя и пароля.
 */
public interface UserPasswdAuthToken extends AuthToken {

    /**
     * Имя пользователя
     */
    String getUsername();

    /**
     * Пароль
     */
    String getPasswd();

}
