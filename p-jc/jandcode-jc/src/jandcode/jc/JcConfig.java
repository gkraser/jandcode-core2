package jandcode.jc;

import java.util.*;

/**
 * config для jc
 */
public interface JcConfig {

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

    /**
     * Режим запуска: продукт. В этом режиме запускается для обеспечения
     * работы в качестве запускалки команд в готовом продукте.
     * По умолчанию: false.
     */
    boolean isRunAsProduct();

    void setRunAsProduct(boolean v);

}
