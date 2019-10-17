package jandcode.core.jsa.jsmodule;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.*;

/**
 * Фабрика модулей
 */
public interface JsModuleFactory extends Comp {

    /**
     * Создать экземпляр модуля
     *
     * @param f         файл, для которого создается модуль
     * @param moduleCfg конфигурация модуля, может быть null
     */
    JsModule createInst(VirtFile f, Conf moduleCfg);

    /**
     * Класс модуля
     */
    Class getModuleClass();

    /**
     * Может ли эта фабрика создавать модули с указанными именем
     */
    boolean isCanCreate(String moduleName);

}
