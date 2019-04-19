package jandcode.mdoc.jc

import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

/**
 * Генерирует конфигурацию запуска jc mdoc-serve из idea
 * для всех проектов mdoc. Используется в корневом проекте.
 */
class GenIdea_RunMDoc extends ProjectScript {

    protected void onInclude() throws Exception {
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x

        int port = 4000

        ListLib rlibs = ctx.getLibs()
        for (Lib lib : rlibs) {
            if (lib.sourceProject != null) {
                Project p = lib.sourceProject
                MDocProject mp = p.getIncluded(MDocProject)
                if (mp != null) {
                    x.addRunConfig_jc("mdoc serve: ${p.name}", "mdoc-serve -p:${port}", p.wd())
                    port++
                }
            }
        }

    }

}
