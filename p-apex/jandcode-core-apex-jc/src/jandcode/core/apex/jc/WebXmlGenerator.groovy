package jandcode.core.apex.jc

import jandcode.commons.simxml.*
import jandcode.core.web.webxml.*
import jandcode.jc.*

class WebXmlGenerator extends ProjectScript {

    void generate(String outFile) {
        outFile = wd(outFile)
        WebXml wx = new DefaultWebXmlFactory().createWebXml()
        SimXml x = new WebXmlUtils().saveToXml(wx)
        x.save().toFile(outFile)
    }

}
