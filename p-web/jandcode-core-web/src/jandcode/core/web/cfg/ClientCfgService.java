package jandcode.core.web.cfg;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Сервис для предоставления конфигурации для клиентского приложения
 */
public interface ClientCfgService extends Comp {

    /**
     * Для всех сервисов приложения, которые реализуют {@link ClientCfgProvider},
     * вызывает метод {@link ClientCfgProvider#grabClientCfg(Conf)}.
     * Полученный результат возвращает.
     * <p>
     * Метод ничего не кеширует. Формировать конфигурацию можно с учетом
     * текущей http-сессии, например учитывать конфиг пользователя.
     * <p>
     * Возвращаемый объект будет преобразован в json и доставлен клиенту.
     * Поэтому стоит использовать только простые значения, которые
     * однозначно можно в json преобразовать.
     */
    Conf grabClientCfg();

}
