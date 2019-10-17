package jandcode.core.jsa.jsmodule;

import jandcode.commons.conf.*;
import jandcode.core.web.virtfile.*;

/**
 * Интерфейс для построения модуля
 */
public interface JsModuleBuilder {

    /**
     * Для какого модуля
     */
    JsModule getModule();

    /**
     * Конфигурация модуля
     */
    Conf getConf();

    /**
     * Установить текст модуля
     */
    void setText(String text);

    /**
     * Текст модуля
     */
    String getText();

    /**
     * Добавить файл, изменения в котором нужно учитывать при определении
     * модификации модуля.
     *
     * @param path виртуальный путь файла
     */
    void addModifyDepend(String path);

    /**
     * Добавить зависимость для модуля
     *
     * @param path путь как он бы был указан в функции require модуля
     */
    void addRequire(String path);

    /**
     * Установить признак динамического модуля.
     */
    void setDynamic(boolean v);

    /**
     * Найти скомпилированный файл для модуля.
     * Например для файла mod/a.js скомпиоированный будет искаться как
     * _jsa/_compiled/mod/a.js--compiled. Если указан суффикс:
     * _jsa/_compiled/mod/a.js--compiled-SUFF
     *
     * @param suff суффикс.
     * @return null, если нет такого файла
     */
    VirtFile findCompiled(String suff);
}
