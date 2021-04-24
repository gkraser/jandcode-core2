package jandcode.core.web.std.gsp;

import jandcode.commons.conf.*;
import jandcode.core.web.cfg.*;
import jandcode.core.web.gsp.*;

/**
 * Сервисные методы для использования в index.gsp и аналогичных файлах,
 * которые формируют стартовый html-файл для js-приложения.
 */
public interface JsIndexGspContext extends IGspContextLinkSet {

    /**
     * Заголовок для страницы
     */
    String getTitle();

    void setTitle(String title);

    /**
     * Конфигурация для клиента. Собирается с использованием
     * {@link ClientCfgService}. Можно менять.
     */
    Conf getCfg();

    /**
     * Конфигурация для клиента {@link JsIndexGspContext#getCfg} в виде json-строки.
     */
    String getCfgJson();


}
