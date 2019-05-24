package jandcode.jc;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;

/**
 * Информация о модуле, который находится в библиотеке
 */
public interface LibModuleInfo extends INamed {

    /**
     * Конфигурация модуля.
     * Загружена из module.cfx.
     */
    Conf getConf();

}
