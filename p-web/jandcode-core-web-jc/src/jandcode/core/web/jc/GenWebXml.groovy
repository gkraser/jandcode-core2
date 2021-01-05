package jandcode.core.web.jc

import jandcode.commons.simxml.*
import jandcode.core.web.webxml.*
import jandcode.jc.*

/**
 * Генератор web.xml
 */
class GenWebXml extends ProjectScript {

    protected void onInclude() throws Exception {
        cm.add("gen-web-xml", "Генерировать файл web.xml", this.&cmGenWebXml,
                cm.opt("to", "temp/web.xml", "Какой файл (по умолчанию temp/web.xml)")
        )
    }

    void cmGenWebXml(CmArgs args) {
        String destFile = args.getString("to", "temp/web.xml")
        genWebXml(destFile)
    }

    /**
     * Генерировать стандартный web.xml для jandcode в указанный файл
     */
    void genWebXml(String destFile) {
        def outFile = wd(destFile)
        WebXml wx = new DefaultWebXmlFactory().createWebXml()
        SimXml x = new WebXmlUtils().saveToXml(wx)
        x.save().toFile(outFile)
        log "gen web.xml: ${outFile}"
    }

}
