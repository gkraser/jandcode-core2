package jandcode.core.apex.jc

import jandcode.commons.*
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

        // logback
        if (UtFile.exists(wd("logback.xml")) && !UtFile.exists(wd("_logback.xml"))) {
            ant.copy(file: wd("logback.xml"), tofile: wd("_logback.xml"))
        }

        // _app.cfx
        if (!UtFile.exists(wd("_app.cfx"))) {
            UtFile.saveString("""\
<?xml version="1.0" encoding="utf-8"?>
<root>
</root>
""", new File(wd("_app.cfx")))
        }

    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        ApexIdeaUtils aie = include(ApexIdeaUtils)
        //
        aie.addRunConfig_ajc(x, "ajc", "")
        aie.addRunConfig_ajc(x, "ajc web-run", "web-run")
    }

}
