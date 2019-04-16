package jandcode.wax.jc

import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jsa.jc.*
import jandcode.web.jc.*

/**
 * Поддержка wax-проекта
 */
class WaxProject extends ProjectScript {

    protected void onInclude() throws Exception {
        //
        include(AppProject)
        include(WebProject)
        include(RootProject)
        include(JsaRootProject)
        include(WaxProductBuilder)
        //
    }

    ////// delegates

    /**
     * Список модулей проекта в правильном порядке (сначала зависимые, потом зависящие).
     * Это имена каталогов модулей относительно корневого проекта.
     */
    List getModules() {
        RootProject rp = include(RootProject)
        return rp.modules
    }

    /**
     * Зависимости для проекта в целом
     */
    LibDepends getDepends() {
        RootProject rp = include(RootProject)
        return rp.getDepends()
    }

}
