package jandcode.core.std;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Элемент конфигурации приложения.
 * Специальный тип компонентов, которые интерпретируют параметры в
 * {@link CfgService} удобным и типизированным способом.
 * <p>
 * Этот интерфейс должен реализовывать бин приложения.
 */
public interface Cfg extends Comp, IConfLink {

    /**
     * Ссылка на {@link CfgService#getConf()}
     */
    Conf getConf();

    /**
     * Ссылка на сервис конфигурации
     */
    CfgService getCfgService();

}
