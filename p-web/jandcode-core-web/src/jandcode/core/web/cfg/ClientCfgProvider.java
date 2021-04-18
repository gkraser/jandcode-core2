package jandcode.core.web.cfg;

import jandcode.commons.conf.*;

/**
 * Провайдер для предоставления конфигурации для клиента.
 * Этот интерфейс должен реализовать сервис приложения, который хочет предоставить
 * конфигурацию для клиента.
 */
public interface ClientCfgProvider {

    /**
     * В переданную cfg необходимо записать конфигурацию,
     * которая будет доступна клиенту.
     */
    void grabClientCfg(Conf cfg) throws Exception;

}
