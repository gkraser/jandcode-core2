package jandcode.core.auth;

import java.util.*;

/**
 * Реализация по умолчанию для аутентифицированных пользователей
 */
public class DefaultAuthUser extends BaseAuthUser {

    public DefaultAuthUser(Map<String, Object> attrs) {
        if (attrs != null) {
            getAttrsHolder().putAll(attrs);
        }
    }

}
