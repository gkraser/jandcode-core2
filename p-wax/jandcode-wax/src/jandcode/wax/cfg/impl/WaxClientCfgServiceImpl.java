package jandcode.wax.cfg.impl;

import jandcode.core.*;
import jandcode.wax.cfg.*;

import java.util.*;

public class WaxClientCfgServiceImpl extends BaseComp implements WaxClientCfgService {

    public Map<String, Object> grabClientCfg() throws Exception {
        Map<String, Object> res = new LinkedHashMap<>();
        for (WaxClientCfgProvider p : getApp().impl(WaxClientCfgProvider.class)) {
            p.grabClientCfg(res);
        }
        return res;
    }

}
