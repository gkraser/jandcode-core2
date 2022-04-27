package jandcode.core.apx.auth;

import jandcode.commons.*;
import jandcode.core.auth.*;
import jandcode.core.auth.std.*;
import jandcode.core.std.*;
import jandcode.core.web.*;
import jandcode.core.web.filter.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Фильтр автологина.
 * В режиме dev выполняется один раз для адреса, откуда был запрос, и логинится настроенным
 * в конфигурации пользователем.
 * <p>
 * Настройки:
 * <p>
 * app.cfx: cfg/auth/autologin/username
 * Имя пользователя. При пустом значении - автологин отключается.
 * <p>
 * app.cfx: cfg/auth/autologin/password
 * Пароль пользователя
 * <p>
 * app.cfx: cfg/auth/autologin/real
 * Если значение true, то выполняется реальный запрос на аутентификацию пользователя,
 * иначе создается пользователь-заглушка admin со всеми правами
 */
public class AuthAutoLoginFilter extends BaseFilter {

    protected static Logger log = LoggerFactory.getLogger(AuthAutoLoginFilter.class);

    // клиенты, которые возпользовались autologin-ом
    protected Map<String, String> usedAddrs = new ConcurrentHashMap<>();

    public void execFilter(FilterType type, Request request) throws Exception {
        if (type == FilterType.startRequest) {
            if (getApp().getEnv().isDev()) {
                AuthUser user = (AuthUser) request.getSession().get(AuthConsts.SESSION_KEY_USER);
                if (user == null) {
                    autoLogin(request);
                }
            }
        }
    }

    protected void autoLogin(Request request) throws Exception {
        // выполняем один раз для одного адреса в рамках экземпляра приложения
        String key = request.getHttpRequest().getRemoteAddr();
        String flag = usedAddrs.get(key);
        if (flag != null) {
            return;
        }
        usedAddrs.put(key, "1");
        //
        AuthUser u = doAutoLogin();
        request.getSession().put(AuthConsts.SESSION_KEY_USER, u);
    }

    /**
     * Выполнить автологин.
     */
    protected AuthUser doAutoLogin() throws Exception {
        CfgService cfgSvc = getApp().bean(CfgService.class);
        AuthService authSvc = getApp().bean(AuthService.class);
        //
        String username = cfgSvc.getConf().getString("auth/autologin/username");
        String password = cfgSvc.getConf().getString("auth/autologin/password");
        boolean real = cfgSvc.getConf().getBoolean("auth/autologin/real");
        if (UtString.empty(username)) {
            log.info("autologin username not defined in 'cfg/auth/autologin/username'");
            return null;
        }
        if (!real) {
            log.info("autologin for dummy user: {}", username);
            AuthUser u = createDummyUser(username);
            log.info("autologin ok");
            return u;
        } else {
            log.info("autologin for real user: {}", username);
            try {
                AuthUser u = authSvc.login(new DefaultUserPasswdAuthToken(username, password));
                log.info("autologin ok");
                return u;
            } catch (Exception e) {
                log.info("autologin error", e);
                return null;
            }
        }
    }

    /**
     * Создание пользоателя-заглушки с правами админа
     */
    public AuthUser createDummyUser(String username) {
        return AuthUserUtils.createAuthUserDummyAdmin(username);
    }

}
