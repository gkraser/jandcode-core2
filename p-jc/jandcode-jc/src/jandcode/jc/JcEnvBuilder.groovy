package jandcode.jc

import jandcode.commons.*
import jandcode.jc.std.*

/**
 * Построитель файла для настройки среды исполнения jc: _jc/jc-env.bat
 * (_jc/jc-env.sh для linux).
 *
 * Делаем include(JcEnvBuilder) в обработчике события
 * {@link PrepareProject.Event_Prepare}. Методом append() добавляем строки,
 * которые должны стать частью файла jc-env.bat/jc-env.sh
 * .
 * Учитываем среду исполнения (windows, linux) {@link jandcode.commons.UtFile#isWindows()}
 */
class JcEnvBuilder extends ProjectScript implements IPrepareProject {

    /**
     * Текст файла jc-env.bat
     */
    StringBuilder text = new StringBuilder()

    void prepareProject() {
        if (this.text.length() == 0) {
            return   // текст не сформирован
        }

        String text1 = ""
        if (UtFile.isWindows()) {
            text1 = "@echo off\n"
        }

        text1 += this.text.toString()

        def fnEnv = UtFile.isWindows() ? "jc-env.bat" : "jc-env.sh"

        ant.mkdir(dir: wd(JcConsts.JC_METADATA_DIR))
        UtFile.saveString(text1, new File(wd(JcConsts.JC_METADATA_DIR + "/${fnEnv}")))
    }

    /**
     * Добавить текст в jc-env.bat/jc-env.sh
     */
    void append(String s) {
        if (this.text.length() > 0) {
            this.text.append("\n")
        }
        this.text.append(s)
    }

}
