package jandcode.core.apex.jc.impl

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.std.*

class AjcGenerator extends ProjectScript {

    void generateAjc(String batFile, String mainClass) {
        generateAjcBat(batFile)
        generateAjcPrepareBat(mainClass)
    }

    void generateAjcBat(batFile) {
        String ajcBatFile = wd(batFile)
        if (!UtFile.exists(ajcBatFile)) {
            log.warn("Файл ${ajcBatFile} не существует и будет создан. Не забудьте его закомитить")
            String srcAjcBatFile = ctx.service(JcDataService).getFile("apex/ajc/ajc-default.bat")
            String txt = UtFile.loadString(srcAjcBatFile)
            UtFile.saveString(txt, new File(ajcBatFile))
        }
    }

    void generateAjcPrepareBat(String mainClass) {
        String ajcPrepareBat = wd("_jc/ajc-prepare.bat")

        String srcAjcPrepareBatFile = ctx.service(JcDataService).getFile("apex/ajc/ajc-prepare-default.bat")
        String txt = UtFile.loadString(srcAjcPrepareBatFile)

        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll
        String cp = UtString.join(rlibs.classpathRaw, ";")

        txt = txt.replace("set MAIN=?", "set MAIN=${mainClass}")
        txt = txt.replace("set CP=?", "set CP=${cp}")

        UtFile.saveString(txt, new File(ajcPrepareBat))
    }

}
