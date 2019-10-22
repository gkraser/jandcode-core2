package jandcode.core.apex.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.core.web.webxml.*
import jandcode.jc.std.*

/**
 * Сборщик продукта для apex-приложения.
 */
class ApexProductBuilder extends ProductBuilder {

    void onExec() {
        // собираем проект
        buildProject()

        // копируем встроенный шаблон содержимого каталога продукта
        copyFolderToDestDir(ctx.service(JcDataService).getFile("apex/product"))

        // копируем шаблон содержимого каталога продукта из проекта
        copyFolderToDestDir("data/product")

        // копируем библиотеки в libs
        def cp = createLibCopier()
        cp.add(include(RootProject).modules)
        cp.add(include(RootProject).depends.prod.names)
        cp.copyTo("${destDir}/lib")

        // дополнительные сгенерированные jar
        //todo jsa
        //ant.copy(file: getIncluded(JsaRootProject).getFileJsaWebrootJar(), todir: "${destDir}/lib")

        // app
        ant.copy(file: wd("app.cfx"), todir: "${destDir}")

        // bat
        makeAjcBat()

        // bat
        makeWebXml()

        // pathprop
        makePathpropFile()

        // version
        makeVersionFile()
    }

    void makeAjcBat() {
        ApexRootProject arp = include(ApexRootProject)
        String destAjcBat = "${destDir}/${arp.ajcBat}"

        if (arp.ajcBat != ApexRootProject.DEFAULT_AJC_BAT) {
            ant.move(file: "${destDir}/${ApexRootProject.DEFAULT_AJC_BAT}",
                    tofile: destAjcBat)
        }

        String txt = UtFile.loadString(destAjcBat)
        txt = txt.replace("set MAIN=?", "set MAIN=${arp.ajcLauncher}")
        UtFile.saveString(txt, new File(destAjcBat))
    }

    void makeWebXml() {
        String wxFile = "${destDir}/web.xml"
        WebXml wx = new DefaultWebXmlFactory().createWebXml()
        SimXml x = new WebXmlUtils().saveToXml(wx)
        x.save().toFile(wxFile)
    }

}
