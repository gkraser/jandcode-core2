package jandcode.jc;

import jandcode.jc.impl.*;

/**
 * Фабрика конфигураций
 */
public class JcConfigFactory {

    /**
     * Создать экземпляр конфигурации
     *
     * @return загруженная конфигурация
     */
    public static JcConfig create() throws Exception {
        return new JcConfigImpl();
    }

}
