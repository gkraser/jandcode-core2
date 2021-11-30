package jandcode.core.web.cfg.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.web.cfg.*;

public class ClientCfgServiceImpl extends BaseComp implements ClientCfgService {

    public Conf grabClientCfg() {
        try {
            Conf res = Conf.create();
            for (ClientCfgProvider p : getApp().impl(ClientCfgProvider.class)) {
                p.grabClientCfg(res);
            }
            return res;
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

}
