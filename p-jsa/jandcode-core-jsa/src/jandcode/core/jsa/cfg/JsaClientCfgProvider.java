package jandcode.core.jsa.cfg;

import java.util.*;

/**
 * Провайдер для предоставления конфигурации для клиента.
 * Этот интерфейс должен реализовать сервис приложения, который хочет предоставить
 * конфигурацию для клиента.
 */
public interface JsaClientCfgProvider {

    /**
     * В переданную cfg необходимо записать конфигурацию,
     * которая будет доступна клиенту.
     */
    void grabClientCfg(Map<String, Object> cfg) throws Exception;

}
