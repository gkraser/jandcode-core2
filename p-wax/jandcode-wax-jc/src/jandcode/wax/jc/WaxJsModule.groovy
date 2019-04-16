package jandcode.wax.jc

import jandcode.jc.*
import jandcode.jsa.jc.*

/**
 * Поддержка js в wax-модуле
 */
class WaxJsModule extends ProjectScript {

    protected void onInclude() throws Exception {
        include(WaxModule)
        include(JsaProject)
    }

}
