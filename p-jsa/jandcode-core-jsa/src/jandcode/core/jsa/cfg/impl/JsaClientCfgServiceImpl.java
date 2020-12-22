package jandcode.core.jsa.cfg.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.jsa.cfg.*;

public class JsaClientCfgServiceImpl extends BaseComp implements JsaClientCfgService {

    public Conf grabClientCfg() {
        try {
            Conf res = UtConf.create();
            for (JsaClientCfgProvider p : getApp().impl(JsaClientCfgProvider.class)) {
                p.grabClientCfg(res);
            }
            return res;
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

}
