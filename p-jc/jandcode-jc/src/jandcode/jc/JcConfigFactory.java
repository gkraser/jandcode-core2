package jandcode.jc;

import jandcode.jc.impl.*;

/**
 * Фабрика конфигураций
 */
public class JcConfigFactory {

    /**
     * Загрузить конфигурацию для указанного рабочего каталога
     *
     * @param workdir рабочий каталог
     * @return загруженная конфигурация
     */
    public static JcConfig load(String workdir) throws Exception {
        JcConfigImpl res = new JcConfigImpl();
        res.load(workdir);
        return res;
    }

}
