package jandcode.core.apex.jc

import jandcode.core.jc.*
import jandcode.core.jsa.jc.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

class ApexRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(RootProject)
        include(AppProject)
        include(AppRunBat)
        include(GenIdea_RunJc)
        include(JsaRootProject)

        // root
        include(RootProject).with {
            depends.prod(
            )
            depends.dev(
                    "jandcode.core.web.tst",
                    "jandcode.core.jsa.tst",
            )
        }

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

        // idea
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)

        // product
        include(AppProductBuilder)
        onEvent(AppProductBuilder.Event_Exec, this.&productHandler)
    }

    //////


    void prepareHandler() {
        log "apex prepare for: ${project.name}"
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x

        //
        include(AppRunBat).addRunConfig(x, "app-run", "-p:8080 -c:/jc -log")
    }

    void productHandler(AppProductBuilder.Event_Exec e) {
        AppProductBuilder builder = e.builder

        // jsa-webroot.jar
        ant.copy(file: include(JsaRootProject).getFileJsaWebrootJar(), todir: "${builder.destDir}/lib")

        // web.xml
        WebXmlGenerator webXmlGenerator = create(WebXmlGenerator)
        webXmlGenerator.generate("${builder.destDir}/web.xml")
    }

}
