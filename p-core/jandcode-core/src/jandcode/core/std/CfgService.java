package jandcode.core.std;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Сервис конфигурации.
 * <p>
 * Конфигураци берется из 'cfg'.
 * В значениях свойств используются подстановки:
 * <ul>
 *     <li>${PATH} - значение из 'cfg/PATH</li>
 * </ul>
 */
public interface CfgService extends Comp, IConfLink {

    /**
     * Конфигурация с раскрытыми подстановками.
     * Подстановки раскрываются в момент конфигурирования сервиса.
     */
    Conf getConf();

    /**
     * Оригинальная конфигурация с нераскрытыми подстановками.
     */
    Conf getOrigConf();

}
