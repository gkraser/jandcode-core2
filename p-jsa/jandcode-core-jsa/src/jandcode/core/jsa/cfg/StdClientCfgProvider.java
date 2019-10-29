package jandcode.core.jsa.cfg;

import jandcode.core.*;

import java.util.*;

/**
 * Стандартный провайдер конфигурации для клиента
 */
public class StdClientCfgProvider extends BaseComp implements JsaClientCfgProvider {

    public void grabClientCfg(Map<String, Object> cfg) throws Exception {
        // debug
        if (getApp().isDebug()) {
            cfg.put("debug", true);
        }
    }

}
