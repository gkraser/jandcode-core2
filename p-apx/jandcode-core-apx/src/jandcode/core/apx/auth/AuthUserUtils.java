package jandcode.core.apx.auth;

import jandcode.commons.*;
import jandcode.core.auth.*;

import java.util.*;

/**
 * Утилиты для AuthUser
 */
public class AuthUserUtils {

    /**
     * Создание экземпляра пользователя-администратора. Используется для заглушек.
     */
    public static AuthUser createAuthUserDummyAdmin(String username) {
        Map<String, Object> attrs = new LinkedHashMap<>();
        //
        if (UtString.empty(username)) {
            username = "admin";
        }
        attrs.put("id", 1L);
        attrs.put("username", username);
        attrs.put("orgId", 1L);
        attrs.put("orgViewId", 1L);
        attrs.put("email", username + "@email.com");
        //
        String fullname = UtString.capFirst(username) + " Administrator";
        attrs.put("fullname", fullname.trim());
        //
        return new DefaultAuthUser(attrs);
    }

}
