package jandcode.core.jsa.jc;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Описание библиотеки nodejs
 */
public interface NodeJsLib extends INamed {

    /**
     * Имя библиотеки.
     * Под этим именем библиотека известна для npm.
     */
    String getName();

    /**
     * Версия библиотеки
     */
    String getVersion();

    /**
     * При значении true - библиотека предназначена для
     * использования на клиенте.
     */
    boolean isClient();

    /**
     * Список масок файлов, которые нужно включить из кода библиотеки для клиента.
     * Маски задаются относительно каталога библиотеки в node_modules.
     * Если библиотека клиентская и это свойство пустое, то
     * по умолчанию включаются все файлы.
     */
    List<String> getIncludeClient();

    /**
     * Список масок файлов, которые нужно исключить из кода библиотеки для клиента.
     * Маски задаются относительно каталога библиотеки в node_modules.
     */
    List<String> getExcludeClient();

    /**
     * Маппинг модулей этой этой библиотеки.
     * Ключ - имя модуля, значение имя другого модуля.
     * Например: ["vue": "vue/dist/vue"].
     * Таким образом, при использовании require('vue'), на самом деле будет
     * вызвано require('vue/dist/vue').
     */
    Map<String, String> getModuleMapping();

    /**
     * Список масок файлов, которые нужно исключить из процесса извлечения require.
     */
    List<String> getExcludeRequire();


}
