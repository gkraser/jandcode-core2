package jandcode.core.web.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.core.web.webxml.*
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
            WebXml wx = new DefaultWebXmlFactory().createWebXml()
            SimXml x = new WebXmlUtils().saveToXml(wx)
            x.save().toFile(wxFile)
        }
        String appFile = UtFile.join(dirWebInf, "app.cfx")
        if (!UtFile.exists(appFile)) {
            SimXml x = new SimXmlNode()
            x.name = "root"
            SimXml x1 = x.addChild("x-include")
            x1.setValue("path", "#{pathprop:jandcode.project.root}/app.cfx")
            x.save().toFile(appFile)
        }
    }

}
