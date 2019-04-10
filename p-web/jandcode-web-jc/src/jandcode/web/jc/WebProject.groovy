package jandcode.web.jc

import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * web-проект
 */
class WebProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(CreateProject)
        include(AppProject)
        include(WebRun)
        include(GenIdea_WebRun)
        include(GenIdea_RunJc)
    }

}
