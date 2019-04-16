package jandcode.wax.jc

import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Поддержка модуля wax-проекта
 */
class WaxModule extends ProjectScript {

    protected void onInclude() throws Exception {
        JavaProject jm = include(JavaProject)
        String moduleName = project.name.replace('-', '.')
        this._mainModule = jm.moduleDef(moduleName)
    }

    /**
     * Имя модуля
     */
    String getModuleName() {
        return _mainModule.getName()
    }

    ////// delegates

    ModuleDefInfo _mainModule

    /**
     * Зависимости модуля.
     */
    LibDepends getDepends() {
        return _mainModule.getDepends()
    }

}
