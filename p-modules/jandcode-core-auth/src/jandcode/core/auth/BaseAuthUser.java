package jandcode.core.auth;

import jandcode.core.auth.impl.*;

/**
 * Базовый предок для аутентифицированных пользователей
 */
public abstract class BaseAuthUser extends CustomAuthUser {

    public boolean isGuest() {
        return false;
    }

}
