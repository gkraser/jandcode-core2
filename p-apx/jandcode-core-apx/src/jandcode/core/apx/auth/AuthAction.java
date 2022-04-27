package jandcode.core.apx.auth;

import jandcode.core.auth.*;
import jandcode.core.auth.std.*;
import jandcode.core.web.action.*;

/**
 * action для аутентификации
 */
public class AuthAction extends BaseAction {

    /**
     * login
     */
    public void login() throws Exception {
        AuthService authSvc = getApp().bean(AuthService.class);
        ActionRequestUtils req = getReq();
        //
        String username = req.getParams().getString("username");
        String password = req.getParams().getString("password");
        //
        AuthUser u = authSvc.login(new DefaultUserPasswdAuthToken(username, password));
        //
        req.getSession().put(AuthConsts.SESSION_KEY_USER, u);
        //
        req.render("ok");
    }

    /**
     * logout
     */
    public void logout() throws Exception {
        ActionRequestUtils req = getReq();
        //
        req.getSession().put(AuthConsts.SESSION_KEY_USER, null);
        req.getHttpRequest().getSession().invalidate();
        //
        req.render("ok");
    }


}
