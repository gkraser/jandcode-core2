package jandcode.core.auth.std;

import jandcode.commons.error.*;

public class AuthError extends XError {

    public AuthError() {
        super("Имя пользователя или пароль не верные");
    }

}
