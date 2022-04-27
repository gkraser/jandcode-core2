package jandcode.core.apx.auth;

import jandcode.core.auth.*;
import jandcode.core.web.*;
import jandcode.core.web.filter.*;

/**
 * Фильтр аутентификации.
 * Если в ключе сессии {@link AuthConsts#SESSION_KEY_USER}
 * определен полльзователь, то он становится текущим пользователем
 * для запроса.
 */
public class AuthFilter extends BaseFilter {

    public void execFilter(FilterType type, Request request) throws Exception {
        if (type == FilterType.startRequest) {
            AuthUser user = (AuthUser) request.getSession().get(AuthConsts.SESSION_KEY_USER);
            getApp().bean(AuthService.class).setCurrentUser(user);

        } else if (type == FilterType.stopRequest) {
            getApp().bean(AuthService.class).setCurrentUser(null);

        }
    }

}
