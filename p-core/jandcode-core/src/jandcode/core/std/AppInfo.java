package jandcode.core.std;

import jandcode.commons.conf.*;
import jandcode.commons.datetime.*;
import jandcode.core.*;

/**
 * Информация о приложении.
 */
public interface AppInfo extends Comp {

    /**
     * Имя главного модуля приложения.
     * Если главный модуль не указан, то в качестве главного
     * назначается модуль jandcode.core
     *
     * <p>
     * module.cfx: app/mainmodule
     */
    String getMainModule();

    /**
     * Заголовок приложения.
     *
     * <p>
     * module.cfx: app/title
     */
    String getTitle();

    /**
     * copyright для приложения.
     *
     * <p>
     * module.cfx: app/copyright
     */
    String getCopyright();

    /**
     * Версия приложения.
     * Определяется по версии главного модуля.
     */
    String getVersion();

    /**
     * Конфигурация app (копия), из которой можно
     * получать дополнительную информацию.
     */
    Conf getConf();

    /**
     * Дата сборки
     */
    XDateTime getBuildDate();

}
