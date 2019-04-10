package jandcode.jc.std

import jandcode.jc.*
import jandcode.jc.std.idea.*

/**
 * Генерирует конфигурацию запуска утилиты jc из idea
 */
class GenIdea_RunJc extends ProjectScript {

    protected void onInclude() throws Exception {
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        x.addRunConfig_jc("jc", "-h")
    }

}
