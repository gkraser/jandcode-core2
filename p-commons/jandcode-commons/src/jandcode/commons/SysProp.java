package jandcode.commons;

import jandcode.commons.env.*;

/**
 * Системные свойства.
 * Все введенные системные свойства описываются в интерфейсах, которые наследуют этот.
 * Таким образом можно иметь единый реестр всех используемых свойств.
 */
public interface SysProp {

    /**
     * Свойство, которое определяет {@link Env#isSource()}
     */
    String PROP_ENV_SOURCE = "jandcode.env.source";

    /**
     * Свойство, которое определяет {@link Env#isDev()}
     */
    String PROP_ENV_DEV = "jandcode.env.dev";

    /**
     * Системное свойство - каталог приложения. Считается, что это корневой каталог
     * проиложения, место куда оно установлено.
     */
    String PROP_APP_DIR = "jandcode.app.appdir";


}
