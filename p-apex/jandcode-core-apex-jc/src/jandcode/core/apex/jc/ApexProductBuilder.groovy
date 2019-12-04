package jandcode.core.apex.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.core.jsa.jc.*
import jandcode.core.web.webxml.*
import jandcode.jc.std.*

/**
 * Сборщик продукта для apex-приложения.
 */
class ApexProductBuilder extends ProductBuilder {

    void onExec() {
        // собираем проект
        buildProject()

        // копируем шаблон содержимого каталога продукта из проекта
        copyFolderToDestDir("data/product")

        // копируем библиотеки в libs
        def cp = createLibCopier()
        cp.add(include(RootProject).modules)
        cp.add(include(RootProject).depends.prod.names)
        cp.copyTo("${destDir}/lib")

        // дополнительные сгенерированные jar
        if (getIncluded(JsaRootProject) != null) {
            ant.copy(file: getIncluded(JsaRootProject).getFileJsaWebrootJar(), todir: "${destDir}/lib")
        }

        // app
        ant.copy(file: wd("app.cfx"), todir: "${destDir}")

        // logback
        if (UtFile.exists(wd("logback.xml"))) {
            ant.copy(file: wd("logback.xml"), todir: "${destDir}")
        }

        // bat
        makeAjcBat()
        makeAjcSh()

        // bat
        makeWebXml()

        // version
        makeVersionFile()
    }

    void makeAjcBat() {
        ApexRootProject arp = include(ApexRootProject)
        String destAjcBat = "${destDir}/${arp.ajcBat}"
        if (UtFile.exists(destAjcBat)) {
            return
        }
        String srcAjcBatFile = ctx.service(JcDataService).getFile("apex/ajc/ajc-product-default.bat")
        String txt = UtFile.loadString(srcAjcBatFile)
        txt = txt.replace("MAIN=?", "MAIN=${arp.ajcLauncher}")
        UtFile.saveString(txt, new File(destAjcBat))
    }

    void makeAjcSh() {
        ApexRootProject arp = include(ApexRootProject)
        String destAjcBat = "${destDir}/${UtFile.removeExt(arp.ajcBat) + '.sh'}"
        if (UtFile.exists(destAjcBat)) {
            return
        }
        String srcAjcBatFile = ctx.service(JcDataService).getFile("apex/ajc/ajc-product-default.sh")
        String txt = UtFile.loadString(srcAjcBatFile)
        txt = txt.replace("MAIN=?", "MAIN=${arp.ajcLauncher}")
        UtFile.saveString(txt, new File(destAjcBat))
    }

    void makeWebXml() {
        String wxFile = "${destDir}/web.xml"
        WebXml wx = new DefaultWebXmlFactory().createWebXml()
        SimXml x = new WebXmlUtils().saveToXml(wx)
        x.save().toFile(wxFile)
    }

}
