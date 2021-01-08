package jandcode.core.web.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Каталог web
 */
class WebDir extends ProjectScript {

    String dir = "web"

    protected void onInclude() throws Exception {
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare) {
            update()
        }
    }

    /**
     * Обновить каталог web и создать то, чего не хватает
     */
    void update() {
        String dirRoot = wd(dir)
        String dirWebInf = UtFile.join(dirRoot, "WEB-INF")
        if (!UtFile.exists(dirWebInf)) {
            ant.mkdir(dir: dirWebInf)
        }
        String wxFile = UtFile.join(dirWebInf, "web.xml")
        if (!UtFile.exists(wxFile)) {
            include(GenWebXml).genWebXml(wxFile)
        }
    }

}
