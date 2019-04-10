package jandcode.web.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*
import jandcode.web.*

/**
 * Генерирует конфигурацию запуска web-приложения из idea через undertow
 */
class GenIdea_WebRun extends ProjectScript {

    protected void onInclude() throws Exception {
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        //
        SimXml z = x.addRunConfig("web-run", ctx.service(JcDataService).getFile("idea/run-main-template.xml"))
        z['singleton'] = true
        z['option@name=MAIN_CLASS_NAME:value'] = WebRunMain.class.getName()

        String p = z['option@name=PROGRAM_PARAMETERS:value']
        if (UtString.empty(p)) {
            z['option@name=PROGRAM_PARAMETERS:value'] = "-p:8080 -c:jc"
        }
    }

}
