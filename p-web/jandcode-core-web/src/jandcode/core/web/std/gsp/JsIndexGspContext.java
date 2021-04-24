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

    ////// links

    /**
     * Добавить js/css в вывод {@link JsIndexGspContext#outLinks()}.
     * css определяется по расширению (css,less,sass,scss).
     */
    void addLink(String path);

    /**
     * Добавить js/css в вывод {@link JsIndexGspContext#outLinks()}.
     * css определяется по расширению (css,less,sass,scss).
     * @param isJs true - это js, иначе - css
     */
    void addLink(String path, boolean isJs);

    /**
     * Вывести все теги для подключения всех модулей.
     * Может включать дополнительный системный вывод.
     */
    void outLinks();

}
