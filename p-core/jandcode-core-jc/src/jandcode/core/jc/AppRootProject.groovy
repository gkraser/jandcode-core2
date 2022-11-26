package jandcode.core.jc

import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Проект app.
 * Приложение core, которое запускается.
 */
class AppRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(AppProject)
        include(GenIdea_RunJc)
        include(AppRunBat)
        //
        include(RootProject)
        include(AppProductBuilder).with {
            name = "app"
        }
        //
        include(AppZipDistrProject)
    }

}
