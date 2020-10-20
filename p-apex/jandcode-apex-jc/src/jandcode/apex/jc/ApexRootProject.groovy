package jandcode.apex.jc

import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Корневой проект для платформы apex.
 * Необходимо включить его в корневой project.jc
 */
class ApexRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(RootProject).with {
            depends.dev(
                    "jandcode.core.web.tst",
                    "jandcode.core.jsa.tst",
                    "jandcode.apex.tst",
            )
        }
    }

}
