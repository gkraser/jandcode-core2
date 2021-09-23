package jandcode.core.apx.jc

import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Корневой проект для платформы apx.
 * Необходимо включить его в корневой project.jc
 */
class ApxRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(RootProject).with {
            depends.dev(
                    "jandcode.core.web.tst",
                    "jandcode.core.apx.tst",
            )
        }
    }

}
