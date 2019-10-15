package jandcode.core;

import jandcode.commons.conf.*;
import jandcode.commons.event.*;

/**
 * Приложение.
 * Конфигурация приложения доступна через метод {@link IConfLink#getConf()}.
 */
public interface App extends IConfLink, BeanFactoryOwner, EventBusOwner {

    /**
     * Модули приложения
     */
    ModuleHolder getModules();

    /**
     * Каталог приложения (где приложение установлено).
     */
    String getAppdir();

    /**
     * Рабочий каталог (где приложение запущено).
     */
    String getWorkdir();

    /**
     * Имя файла, откуда было загружено приложение
     */
    String getAppConfFile();

    /**
     * Имя приложения
     */
    String getAppName();

    /**
     * Возвращает признак отладочного режима
     */
    boolean isDebug();

    /**
     * Признак тестовой среды: приложение создано в unittest.
     */
    boolean isTest();

    /**
     * Запуск приложения.
     * <p>
     * Для реакции на вызов этого метода сервис приложения должен
     * реализовать интерфейс {@link IAppStartup}.
     * <p>
     * Вызывается в приложении (например в сервлете). В тестах и в утилите jc
     * не вызывается. Метод срабатывает только раз. Вызовы startup/shutdown
     * должны быть сбалансированы.
     */
    void startup();

    /**
     * Остановка приложения.
     * <p>
     * Для реакции на вызов этого метода сервис приложения должен
     * реализовать интерфейс {@link IAppShutdown}.
     * <p>
     * Вызывается в приложении (например в сервлете). В тестах и в утилите jc
     * не вызывается. Метод срабатывает только раз. Вызовы startup/shutdown
     * должны быть сбалансированы.
     */
    void shutdown();


}
