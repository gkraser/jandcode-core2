package jandcode.jsa.cfg;

import jandcode.core.*;

import java.util.*;

/**
 * Сервис для предоставления конфигурации для клиентского приложения
 */
public interface JsaClientCfgService extends Comp {

    /**
     * Для всех сервисов приложения, которые реализуют {@link JsaClientCfgProvider},
     * вызывает метод {@link JsaClientCfgProvider#grabClientCfg(java.util.Map)}.
     * Полученный результат возвращает
     */
    Map<String, Object> grabClientCfg() throws Exception;

}
