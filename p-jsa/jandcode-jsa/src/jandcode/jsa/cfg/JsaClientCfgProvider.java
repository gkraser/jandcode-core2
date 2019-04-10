package jandcode.jsa.cfg;

import java.util.*;

/**
 * Провайдер для предоставления конфигурации для клиента
 */
public interface JsaClientCfgProvider {

    /**
     * В переданную cfg необходимо записать конфигурацию,
     * которая будет доступна клиенту.
     */
    void grabClientCfg(Map<String, Object> cfg) throws Exception;

}
