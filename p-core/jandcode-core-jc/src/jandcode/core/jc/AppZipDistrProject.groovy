package jandcode.core.jc

import jandcode.commons.UtFile
import jandcode.jc.*

/**
 * Сборка zip-дистрибутива
 */
class AppZipDistrProject extends ProjectScript {

    protected void onInclude() throws Exception {
        cm.add("distr", "Сборка дистрибутива", this.&cmDistr,
                cm.opt("q", "Быстрая сборка. Пропустить этап компиляции (для отладки)"),
                cm.opt("debug", "debug-сборка. Устанавливается флаг ctx.env.debug"),
                cm.opt("no-clean", "Не очищать каталог _jc/distr"),
        )
        cm.add("distr-debug", "Сборка дистрибутива prod и debug", this.&cmDistrDebug)
    }

    void cmDistr(CmArgs args) {
        boolean debug = args.getBoolean("debug")
        boolean quick = args.getBoolean("q")
        boolean noClean = args.getBoolean("no-clean")

        Map params = [:]
        String suffix = ''
        if (debug) {
            params['debug'] = true
            suffix = '-DEBUG'
        }

        String outDir = wd("_jc/distr")
        String productDir = wd("_jc/product")

        if (!noClean) {
            ut.cleandir(outDir)
        }

        if (!quick) {
            cm.exec("product", params)
        }

        // zip distr
        String zipFile = "${outDir}/${project.name}-${project.version}${suffix}.zip"
        ant.zip(destfile: zipFile) {
            zipfileset(dir: productDir, defaultexcludes: false) {
                include(name: "**/*")
            }
        }
        log "created: ${zipFile}"

        // war distr
        if (UtFile.exists(UtFile.join(productDir, "web.xml"))) {
            String warFile = "${outDir}/${project.name}-${project.version}${suffix}.war"
            ant.zip(destfile: warFile) {
                zipfileset(dir: productDir, defaultexcludes: false, prefix: "WEB-INF") {
                    include(name: "**/*")
                }
            }
            log "created: ${warFile}"
        }

    }

    void cmDistrDebug(CmArgs args) {
        ut.runcmd(cmd: "jc distr")
        ut.runcmd(cmd: "jc distr -debug -no-clean")
    }

}
