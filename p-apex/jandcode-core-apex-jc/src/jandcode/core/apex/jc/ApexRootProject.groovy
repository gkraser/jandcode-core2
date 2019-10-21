package jandcode.core.apex.jc

import jandcode.core.apex.jc.impl.*
import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*

class ApexRootProject extends ProjectScript {

    public static final String DEFAULT_AJC_BAT = "ajc.bat"

    /**
     * Класс с методом main, который запускает приложение
     */
    String ajcLauncher = "ajcLauncher_NOT_DEFINED"

    /**
     * Батник для запуска приложения
     */
    String ajcBat = DEFAULT_AJC_BAT

    protected void onInclude() throws Exception {
        include(RootProject)
        include(AppProject)
        include(ApexProductBuilder)

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

    }

    /**
     * Подготовка проекта к использованию
     */
    void prepareHandler() {
        log "prepare apex"
        create(AjcGenerator).generateAjc(ajcBat, ajcLauncher)
    }

}
