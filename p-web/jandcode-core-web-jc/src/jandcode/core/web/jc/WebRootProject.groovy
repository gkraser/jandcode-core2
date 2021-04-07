package jandcode.core.web.jc


import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*

class WebRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(AppRunBat).with {
            addRunConfig("app-run serve", "serve -log -p 8080 -c /jc")
        }
        include(RootProject).with {
            depends.dev(
                    "jandcode.core.web.tst",
            )
        }
        include(GenWebXml)
        onEvent(AppProductBuilder.Event_Exec, this.&onAppProductBuild)
    }

    void onAppProductBuild(AppProductBuilder.Event_Exec ev) {
        include(GenWebXml).genWebXml("${ev.builder.destDir}/web.xml")
    }

}
