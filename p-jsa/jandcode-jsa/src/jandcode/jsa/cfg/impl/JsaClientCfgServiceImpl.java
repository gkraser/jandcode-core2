package jandcode.jsa.cfg.impl;

import jandcode.core.*;
import jandcode.jsa.cfg.*;

import java.util.*;

public class JsaClientCfgServiceImpl extends BaseComp implements JsaClientCfgService {

    public Map<String, Object> grabClientCfg() throws Exception {
        Map<String, Object> res = new LinkedHashMap<>();
        for (JsaClientCfgProvider p : getApp().impl(JsaClientCfgProvider.class)) {
            p.grabClientCfg(res);
        }
        return res;
    }

}
