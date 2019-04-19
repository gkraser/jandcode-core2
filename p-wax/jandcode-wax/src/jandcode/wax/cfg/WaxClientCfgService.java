package jandcode.wax.cfg;

import jandcode.core.*;

import java.util.*;

/**
 * Сервис для предоставления конфигурации для клиентского приложения
 */
public interface WaxClientCfgService extends Comp {

    /**
     * Для всех сервисов приложения, которые реализуют {@link WaxClientCfgProvider},
     * вызывает метод {@link WaxClientCfgProvider#grabClientCfg(Map)}.
     * Полученный результат возвращает.
     */
    Map<String, Object> grabClientCfg() throws Exception;

}
