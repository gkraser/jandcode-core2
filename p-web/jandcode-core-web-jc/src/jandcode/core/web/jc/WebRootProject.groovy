package jandcode.core.web.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*

class WebRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(AppRunBat).with {
            addRunConfig("app-run serve", "serve -log -p 8080 -c /jc")
        }
        include(RootProject).with {
            depends.dev(
                    "jandcode.core.web.tst",
            )
        }
        include(GenWebXml)
        onEvent(AppProductBuilder.Event_Exec, this.&onAppProductBuild)
    }

    protected List<String> _resourceWebrootPaths = []

    /**
     * Добавить путь, который будет запакован в web-resource-webroot.jar при
     * сборке дистрибутива.
     */
    void addResourceWebroot(String path) {
        _resourceWebrootPaths.add(path)
    }

    /**
     * Собрать web-resource-webroot.jar и вернуть путь до собранного файла.
     * Возвращает null, если собирать нечего.
     */
    String buildResourceWebrootJar() {
        if (_resourceWebrootPaths.size() == 0) {
            return null
        }

        String resourceRoot = "resource-webroot"
        String destFile = wd("temp/lib/web-resource-webroot.jar")
        log.info("build jar: ${destFile}")

        // собираем jar
        ant.jar(destfile: destFile) {
            for (srcPath in this._resourceWebrootPaths) {
                if (!UtFile.exists(srcPath)) {
                    throw new XError("Not found resourceWebrootPath: ${srcPath}")
                }
                zipfileset(dir: srcPath, prefix: "${resourceRoot}") {
                    include(name: '**/*')
                }
            }
        }

        return destFile
    }

    void onAppProductBuild(AppProductBuilder.Event_Exec ev) {
        AppProductBuilder builder = ev.builder

        include(GenWebXml).genWebXml("${ev.builder.destDir}/web.xml")

        // web-resouurce-webroot.jar
        String rwr = buildResourceWebrootJar()
        if (rwr != null) {
            //
            ant.copy(file: rwr, todir: "${builder.destDir}/lib")
        }
    }

}
