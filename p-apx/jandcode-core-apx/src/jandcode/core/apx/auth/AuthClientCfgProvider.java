package jandcode.core.apx.auth;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.auth.*;
import jandcode.core.std.*;
import jandcode.core.web.cfg.*;

/**
 * Предоставляет информацию о пользователе для клиента.
 * Берется информация из узла cfg/client, на нее накладываются атрибуты
 * пользователя. Полученная информация помещается в узел userInfo.
 */
public class AuthClientCfgProvider extends BaseComp implements ClientCfgProvider {

    public void grabClientCfg(Conf cfg) throws Exception {
        CfgService cfgSvc = getApp().bean(CfgService.class);
        AuthUser user = getApp().bean(AuthService.class).getCurrentUser();
        //
        Conf userInfo = cfg.findConf("userInfo", true);
        if (!user.isGuest()) {
            Conf clientConf = cfgSvc.getConf().findConf("client");
            if (clientConf != null) {
                cfg.join(clientConf);
            }
            userInfo.putAll(user.getAttrs());
            userInfo.setValue("guest", false);
        } else {
            userInfo.setValue("guest", true);
        }
    }

}
