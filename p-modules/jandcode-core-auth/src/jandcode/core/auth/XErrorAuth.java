package jandcode.core.auth;

import jandcode.commons.error.*;

/**
 * Универсальная ошибка аутентификации
 */
public class XErrorAuth extends XError {

    public static final String msg_invalid_user_passwd = "Имя пользователя или пароль не верные";
    public static final String msg_locked_user = "Пользователь заблокирован";
    public static final String msg_error = "Ошибка аутентификации";
    public static final String msg_unsupported_token = "Токен аутентификации не поддерживается";

    public XErrorAuth() {
        super(msg_error);
    }

    public XErrorAuth(String message) {
        super(message);
    }

    public XErrorAuth(Throwable cause, String message) {
        super(cause, message);
    }

}
