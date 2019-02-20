package jandcode.jc;

import jandcode.commons.conf.*;

import java.util.*;

/**
 * config для jc
 */
public interface JcConfig {

    /**
     * Конфигурация
     */
    Conf getConf();

    /**
     * Каталог, в котором установлен jc
     */
    String getAppdir();

    /**
     * Установить каталог, в котором установлен jc
     */
    void setAppdir(String path);

    /**
     * Список проектов, которые нужно загрузить перед тем, как будет загружен основной
     * проект.
     */
    List<String> getAutoLoadProjects();

}
