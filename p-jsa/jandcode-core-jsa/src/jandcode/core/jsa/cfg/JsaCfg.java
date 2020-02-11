package jandcode.core.jsa.cfg;

import jandcode.core.jsa.gsp.*;
import jandcode.core.std.*;

/**
 * Конфигурация для jsa
 */
public interface JsaCfg extends Cfg {

    /**
     * Имя модуля, который должен содержать инфраструктуру модульной
     * системы и поддержку сгенеренных модулей (к примеру он должен содержать
     * babelHelpers-инструменты).
     * Он будет вставлен первым первым тегом script как ссылка на текст js-текст.
     * Его содержимое не является модулем.
     * <p>
     * app.cfx: cfg/jsa/bootModule
     */
    String getBootModule();

    /**
     * Модуль формирования среды исполнения.
     * Этот модуль содержит зависимости, которые являются частью
     * используемой библиотеки. Позволяет отделить загрузку модулей
     * приложения от модулей библиотек.
     * <p>
     * app.cfx: cfg/jsa/envModule
     *
     * @see JsaIndexGspContext
     */
    String getEnvModule();

    /**
     * Имя темы по умолчанию.
     * <p>
     * app.cfx: cfg/jsa/defautTheme
     *
     * @see JsaIndexGspContext
     */
    String getDefaultTheme();

}
