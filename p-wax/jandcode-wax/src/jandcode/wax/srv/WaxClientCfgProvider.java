package jandcode.wax.srv;

import java.util.*;

/**
 * Провайдер для предоставления конфигурации для клиента.
 * Этот интерфейс должен реализовать сервис приложения, который делает предоставить
 * конфигурацию для клиента.
 */
public interface WaxClientCfgProvider {

    /**
     * В переданную cfg необходимо записать конфигурацию,
     * которая будет доступна клиенту.
     */
    void grabClientCfg(Map<String, Object> cfg) throws Exception;

}
