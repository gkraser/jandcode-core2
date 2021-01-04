package jandcode.core.web.jc

import jandcode.commons.simxml.*
import jandcode.core.jc.*
import jandcode.core.web.webxml.*
import jandcode.jc.*
import jandcode.jc.std.*

class WebRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(AppRunBat).with {
            addRunConfig("app-run serve", "serve -log -p:8080 -c:/jc")
        }
        include(RootProject).with {
            depends.dev(
                    "jandcode.core.web.tst",
            )
        }
        onEvent(AppProductBuilder.Event_Exec, this.&onAppProductBuild)
    }

    void onAppProductBuild(AppProductBuilder.Event_Exec ev) {
        genWebXml("${ev.builder.destDir}/web.xml")
    }

    void genWebXml(String destFile) {
        def outFile = wd(destFile)
        WebXml wx = new DefaultWebXmlFactory().createWebXml()
        SimXml x = new WebXmlUtils().saveToXml(wx)
        x.save().toFile(outFile)
    }

}
