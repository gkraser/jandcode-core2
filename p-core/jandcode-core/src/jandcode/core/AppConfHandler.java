package jandcode.core;

import jandcode.commons.conf.*;

/**
 * Интерфейс для обработчиков app.cfx и module.cfx.
 * <p>
 * Указывается в module.cfx:
 * <pre>
 * {@code <app-conf-handler name="NAME" class="CLASS" ANY-ATTR="ANY-VALUE"/>}
 * </pre>
 * Каждый обработчик вызывается сначала для каждого модуля:
 * {@link AppConfHandler#handleModuleConf(Module, Module, Conf)},
 * затем создается конфигурация приложения app.getConf() и каждый обработчик вызывается для
 * приложения: {@link AppConfHandler#handleAppConf(App, Module, Conf)}
 */
public interface AppConfHandler {

    /**
     * Обработчик для модуля
     *
     * @param module      для какого модуля. Нужно обрабатывать module.getConf().
     *                    Доступны все модули в module.getApp().getModules().
     * @param moduleOwner модуль, в котором был объявлен тег с обработчиком
     * @param conf        конфигурация. Тег app-conf-handler, в котором описан обработчик
     */
    void handleModuleConf(Module module, Module moduleOwner, Conf conf) throws Exception;

    /**
     * Обработчик для приложения
     *
     * @param app         для какого приложения. Нужно обрабатывать app.getConf().
     *                    Доступны все модули в app.getModules().
     * @param moduleOwner модуль, в котором был объявлен тег с обработчиком
     * @param conf        конфигурация. Тег app-conf-handler, в котором описан обработчик
     */
    void handleAppConf(App app, Module moduleOwner, Conf conf) throws Exception;

}
