package jandcode.core.auth.std;

import jandcode.core.auth.*;

/**
 * Токен авторизации для имени пользователя/пароля.
 */
public class DefaultUserPasswdAuthToken implements UserPasswdAuthToken {

    private String username;
    private String passwd;

    public DefaultUserPasswdAuthToken(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswd() {
        return passwd;
    }
}
