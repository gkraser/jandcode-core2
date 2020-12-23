package jandcode.core.auth.std;

import jandcode.core.*;
import jandcode.core.auth.*;

import java.util.*;

/**
 * Заглушка для {@link AuthProcessor}.
 * <p>
 * Может аутентифицировать пользователей по имени/паролю:
 * admin/111, user1/111.
 */
public class DummyAuthProcessor extends BaseComp implements AuthProcessor {

    public boolean isSupportedAuthToken(AuthToken authToken) {
        return authToken instanceof UserPasswdAuthToken;
    }

    public AuthUser login(AuthToken authToken) throws Exception {
        UserPasswdAuthToken token = (UserPasswdAuthToken) authToken;

        if ("admin".equals(token.getUsername()) && "111".equals(token.getPasswd())) {
            return createUser(token.getUsername(), 1);

        } else if ("user1".equals(token.getUsername()) && "111".equals(token.getPasswd())) {
            return createUser(token.getUsername(), 101);

        } else {
            throw new XErrorAuth(XErrorAuth.msg_invalid_user_passwd);
        }

    }

    protected AuthUser createUser(String username, long id) {
        Map<String, Object> attrs = new LinkedHashMap<>();
        attrs.put("id", id);
        attrs.put("username", username);
        return new DefaultAuthUser(attrs);
    }

}
