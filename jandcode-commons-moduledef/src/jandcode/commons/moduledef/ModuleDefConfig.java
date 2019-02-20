package jandcode.commons.moduledef;

import jandcode.commons.conf.*;

import java.util.*;

/**
 * Загружаемая конфигурация модуля
 */
public interface ModuleDefConfig {

    /**
     * Конфигурация модуля
     */
    Conf getConf();

    /**
     * Список файлов, которые были загружены для формирования конфигурации.
     */
    List<String> getFiles();

    /**
     * Зависимости модуля, которые выявлены в процессе загрузки конфигурации
     */
    List<String> getDepends();

}
