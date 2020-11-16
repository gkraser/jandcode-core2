package jandcode.core.dbm.domain.impl;

import jandcode.commons.conf.*;

/**
 * Стандартный сборщик структур доменов и полей из модели
 */
public class DomainConfGrab {

    /**
     * Собрать все нужное из конфигурации модели
     *
     * @param modelConf конфигурация модели
     * @param root      куда собирать
     */
    public void grab(Conf modelConf, Conf root) {

        // определяем порядок, что бы смотреть было удобнее
        root.findConf("system", true);
        root.findConf("field", true);
        root.findConf("domain", true);

        String[] tags = {"domain", "field"};
        for (String tag : tags) {
            Conf x = modelConf.findConf(tag);
            if (x != null) {
                Conf x1 = root.findConf(tag, true);
                x1.join(x);
            }
        }
    }

}
