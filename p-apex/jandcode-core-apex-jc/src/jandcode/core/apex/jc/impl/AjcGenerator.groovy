package jandcode.core.apex.jc.impl

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.std.*

class AjcGenerator extends ProjectScript {

    void generateAjc(String mainClass) {
        generateAjcBat(mainClass)
        generateAjcRunnerBat()
    }

    void generateAjcBat(String mainClass) {
        String ajcBatFile = wd("ajc.bat")
        if (!UtFile.exists(ajcBatFile)) {
            log.warn("Файл ${ajcBatFile} не существует и будет создан. Не забудьте его закомитить")
            String srcAjcBatFile = ctx.service(JcDataService).getFile("apex/ajc-default.bat")
            String txt = UtFile.loadString(srcAjcBatFile)
            txt = txt.replace("set MAIN=?", "set MAIN=${mainClass}")
            UtFile.saveString(txt, new File(ajcBatFile))
        }
    }

    void generateAjcRunnerBat() {
        String ajcPrepareBat = wd("_jc/ajc-prepare.bat")
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll
        String cp = UtString.join(rlibs.classpathRaw, ";")
        UtFile.saveString("""\
@echo off
set CP=${cp}
""", new File(ajcPrepareBat))
    }

}
