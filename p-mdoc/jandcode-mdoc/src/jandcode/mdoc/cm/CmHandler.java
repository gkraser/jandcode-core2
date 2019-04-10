package jandcode.mdoc.cm;

import jandcode.commons.variant.*;
import jandcode.mdoc.builder.*;

/**
 * Обработчик команд в теге cm
 */
public interface CmHandler {

    /**
     * Обработчик команды
     *
     * @param attrs   атрибуты
     * @param outFile для кого
     * @return текст, который будет вставлен в html вместо тега cm
     */
    String handleCm(IVariantMap attrs, OutFile outFile) throws Exception;

}
