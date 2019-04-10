package jandcode.jc.std

import jandcode.jc.*

/**
 * Очистка каталогов projecttml.
 * Используется в корне каталога jc-data, который содержит projecttml
 */
class CleanProjecttml extends ProjectScript {

    protected void onInclude() throws Exception {
        include(CleanProject)
        onEvent(CleanProject.Event_Clean, this.&cleanHandler)
    }

    void cleanHandler() {
        // чистим все -projecttml.jc

        def scanner = ant.fileset(dir: wd) {
            include(name: "**/" + Projecttml.PROJECTTML_FILE)
        }
        for (f in scanner) {
            def pf = f.toString()

            Project p = load(pf)
            if (p.cm.find("clean")) {
                p.cm.exec("clean")
            }

        }

    }

}
