package jandcode.core.auth.impl;

import jandcode.core.*;
import jandcode.core.auth.*;

public class AuthServiceImpl extends BaseComp implements AuthService {

    private UserLinkThreadLocal userLinkThreadLocal = new UserLinkThreadLocal();

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

    public AuthUser getCurrentUser() {
        return userLinkThreadLocal.get().getUser();
    }

    public void setCurrentUser(AuthUser user) {
        userLinkThreadLocal.get().setUser(user);
    }

}
