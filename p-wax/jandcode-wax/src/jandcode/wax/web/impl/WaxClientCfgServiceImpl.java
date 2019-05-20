package jandcode.wax.web.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.wax.web.*;

import java.util.*;

public class WaxClientCfgServiceImpl extends BaseComp implements WaxClientCfgService {

    public Map<String, Object> grabClientCfg() {
        try {
            Map<String, Object> res = new LinkedHashMap<>();
            for (WaxClientCfgProvider p : getApp().impl(WaxClientCfgProvider.class)) {
                p.grabClientCfg(res);
            }
            return res;
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

}
