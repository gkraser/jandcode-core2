package jandcode.xcore;

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


    ////// Events

    /**
     * Событие вызывается после полной загрузки приложения, как последний этап.
     */
    class Event_AppLoaded implements Event {
    }

    /**
     * Событие: приложение закончило работу.
     * Вызывается именно в приложении (например в сервлете). В тестах и в утилите jc
     * не трогается.
     */
    class Event_AppShutdown implements Event {
    }

    /**
     * Событие: приложение стартовало.
     * Вызывается именно в приложении (например в сервлете). В тестах и в утилите jc
     * не трогается.
     */
    class Event_AppStartup implements Event {
    }

}
