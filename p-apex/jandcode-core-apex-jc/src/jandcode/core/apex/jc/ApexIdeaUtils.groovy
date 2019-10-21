package jandcode.core.apex.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

class ApexIdeaUtils extends ProjectScript {

    /**
     * Добавить конфигурацию запуска утилиты jc
     * @param name имя конфигурации
     * @param cliArguments аргументы командной строки запуска jc
     * @param vmParameters аргументы для виртуальной машины
     * @return добавленная конфигурация, можно настраивать далее
     */
    SimXml addRunConfig_ajc(IprXml x, String name, String cliArguments, String vmParameters = "") {
        ApexRootProject apr = include(ApexRootProject)
        SimXml z = x.addRunConfig(name, ctx.service(JcDataService).getFile("idea/run-main-template.xml"))

        z['option@name=MAIN_CLASS_NAME:value'] = apr.ajcLauncher

        String vma = ""
        vma += " -D" + JcConsts.PROP_APP_DIR + "=" + wd
        if (!UtString.empty(vmParameters)) {
            vma += " " + vmParameters
        }

        z['option@name=VM_PARAMETERS:value'] = vma.trim()
        z['option@name=PROGRAM_PARAMETERS:value'] = cliArguments.trim()
        z['option@name=WORKING_DIRECTORY:value'] = wd

        //
        return z
    }


}
