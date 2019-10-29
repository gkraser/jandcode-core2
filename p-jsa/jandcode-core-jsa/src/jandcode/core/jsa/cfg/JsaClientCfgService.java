package jandcode.core.jsa.cfg;

import jandcode.core.*;

import java.util.*;

/**
 * Сервис для предоставления конфигурации для клиентского приложения
 */
public interface JsaClientCfgService extends Comp {

    /**
     * Для всех сервисов приложения, которые реализуют {@link JsaClientCfgProvider},
     * вызывает метод {@link JsaClientCfgProvider#grabClientCfg(Map)}.
     * Полученный результат возвращает.
     * <p>
     * Метод ничего не кеширует. Формировать конфигурацию можно с учетом
     * текущей http-сессии, например учитывать конфиг пользователя.
     * <p>
     * Возвращаемый объект бедет преобразован в json и доставлен клиенту.
     * Поэтому стоит использовать только простые значения, которые
     * однозначно можно в json преобразовать.
     */
    Map<String, Object> grabClientCfg();

}
