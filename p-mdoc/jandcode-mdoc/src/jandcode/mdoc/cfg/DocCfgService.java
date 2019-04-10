package jandcode.mdoc.cfg;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Сервис поддержки конфигурирования
 */
public interface DocCfgService extends Comp {

    /**
     * Создать FilesetCfg по conf
     */
    FilesetCfg createFilesetCfg(Conf conf);

    /**
     * Создать DocCfg по conf
     */
    DocCfg createDocCfg(Conf conf);

}
