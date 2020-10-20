package jandcode.apex.jc

import jandcode.core.jc.*
import jandcode.core.jsa.jc.*
import jandcode.jc.*

/**
 * Корневой проект для платформы apex.
 * Необходимо включить его в корневой project.jc
 */
class ApexRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        // product
        onEvent(AppProductBuilder.Event_Exec, this.&productHandler)
    }

    //////

    void productHandler(AppProductBuilder.Event_Exec e) {
        AppProductBuilder builder = e.builder

        // jsa-webroot.jar
        if (getIncluded(JsaRootProject) != null) {
            ant.copy(file: include(JsaRootProject).getFileJsaWebrootJar(), todir: "${builder.destDir}/lib")
        }
    }

}
