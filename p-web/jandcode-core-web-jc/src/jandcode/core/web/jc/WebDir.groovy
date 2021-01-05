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
        String appFile = UtFile.join(dirWebInf, "app.cfx")
        if (!UtFile.exists(appFile)) {
            SimXml x = new SimXmlNode()
            x.name = "root"
            SimXml x1 = x.addChild("x-include")
            x1.setValue("path", "#{appdir}/app.cfx")
            x.save().toFile(appFile)
        }
    }

}
