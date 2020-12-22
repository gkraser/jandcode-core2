package jandcode.core.auth.impl;

import jandcode.core.*;
import jandcode.core.auth.*;
import jandcode.core.auth.std.*;

public class AuthServiceImpl extends BaseComp implements AuthService {

    private UserLinkThreadLocal userLinkThreadLocal = new UserLinkThreadLocal();
    private AuthProcessor authProcessor;

    class UserLink {

        AuthUser user;

        AuthUser getUser() {
            if (user == null) {
                user = new GuestAuthUser();
            }
            return user;
        }

        public void setUser(AuthUser user) {
            this.user = user;
        }
    }

    class UserLinkThreadLocal extends ThreadLocal<UserLink> {
        protected UserLink initialValue() {
            return new UserLink();
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.authProcessor = (AuthProcessor) getApp().create(cfg.getConf().getConf("auth-processor/default"));
    }

    public AuthUser getCurrentUser() {
        return userLinkThreadLocal.get().getUser();
    }

    public void setCurrentUser(AuthUser user) {
        userLinkThreadLocal.get().setUser(user);
    }

    //////

    public AuthProcessor getAuthProcessor() {
        return authProcessor;
    }

    public AuthUser login(AuthToken authToken) throws Exception {
        if (!getAuthProcessor().isSupportedAuthToken(authToken)) {
            throw new AuthError();
        }
        return getAuthProcessor().login(authToken);
    }

}
