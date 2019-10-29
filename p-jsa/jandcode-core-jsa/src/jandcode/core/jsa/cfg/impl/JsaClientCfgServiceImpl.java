package jandcode.core.jsa.cfg.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.jsa.cfg.*;

import java.util.*;

public class JsaClientCfgServiceImpl extends BaseComp implements JsaClientCfgService {

    public Map<String, Object> grabClientCfg() {
        try {
            Map<String, Object> res = new LinkedHashMap<>();
            for (JsaClientCfgProvider p : getApp().impl(JsaClientCfgProvider.class)) {
                p.grabClientCfg(res);
            }
            return res;
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

}
