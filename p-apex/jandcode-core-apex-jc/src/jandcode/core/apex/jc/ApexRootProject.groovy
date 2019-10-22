package jandcode.core.apex.jc


import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

class ApexRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(RootProject)
        include(AppProject)
        include(ApexProductBuilder)

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

        // idea
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)

    }

    //////

    public static final String DEFAULT_AJC_BAT = "ajc.bat"

    /**
     * Класс с методом main, который запускает приложение
     */
    String ajcLauncher = "ajcLauncher_NOT_DEFINED"

    /**
     * Батник для запуска приложения
     */
    String ajcBat = DEFAULT_AJC_BAT

    void prepareHandler() {
        log "prepare apex"
        create(AjcGenerator).generateAjc(ajcBat, ajcLauncher)
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        ApexIdeaUtils aie = include(ApexIdeaUtils)
        //
        aie.addRunConfig_ajc(x, "ajc", "")
        aie.addRunConfig_ajc(x, "ajc web-run", "web-run")
    }

}
