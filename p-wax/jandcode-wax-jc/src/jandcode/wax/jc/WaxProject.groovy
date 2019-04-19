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
        include(CreateProject)
        include(AppProject)
        include(WebRun)
        include(GenIdea_WebRun)
        include(GenIdea_RunJc)
        include(RootProject)
        include(JsaRootProject)
        include(WaxProductBuilder)
        //
    }

}
