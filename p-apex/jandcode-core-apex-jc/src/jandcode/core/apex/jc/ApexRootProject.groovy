package jandcode.core.apex.jc

import jandcode.core.apex.jc.impl.*
import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*

class ApexRootProject extends ProjectScript {

    /**
     * Класс с методом main, который запускает приложение
     */
    String ajcLauncher = "ajcLauncher_NOT_DEFINED"

    protected void onInclude() throws Exception {
        include(RootProject)
        include(AppProject)

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

    }

    /**
     * Подготовка проекта к использованию
     */
    void prepareHandler() {
        log "prepare apex"
        create(AjcGenerator).generateAjc(ajcLauncher)
    }

}
