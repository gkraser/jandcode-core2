package jandcode.core.jc;

import jandcode.commons.simxml.*;

/**
 * Интерфейс для обновления содержимого файла _app.cfx при prepare
 */
public interface AppCfxBuilderHandler {

    /**
     * Обновить содержимое файла _app.cfx
     *
     * @param x загруженный файл _app.cfx
     */
    void updateContent(SimXml x);

}
