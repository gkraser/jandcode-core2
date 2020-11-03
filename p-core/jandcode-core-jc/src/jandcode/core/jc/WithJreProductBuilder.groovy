package jandcode.core.jc

import jandcode.jc.std.*

/**
 * Сборка приложения вместе с jre.
 * Запускается после сборки приложения.
 *
 */
class WithJreProductBuilder extends ProductBuilder {

    /**
     * Имя builder, который собрал app
     */
    String appBuilderName = "app"

    void onExec() {
        log "Считаем, что продукт уже собран"
        ProductBuilder appBuilder = getBuilder(appBuilderName)

        log "Собираем все зависимости от jdk"
        String cmd = "jdeps --multi-release 14 --print-module-deps --ignore-missing-deps ${appBuilder.destDir}/lib/*.jar"
        List<String> z = ut.runcmd(cmd: cmd, showout: true, saveout: true)
        String modules = z[0]

        log "Собираем jre"
        cmd = "jlink --add-modules ${modules} --output ${destDir}/jre"
        ut.runcmd(cmd: cmd)

        log "Копируем приложение"
        ant.copy(todir: "${destDir}/app") {
            fileset(dir: "${appBuilder.destDir}")
        }

        String appRun = """\
@echo off
set JAVA_HOME=%~dp0jre
set PATH=%JAVA_HOME%\\bin;%PATH%
call %~dp0app\\app-run.bat %*
"""

        ant.echo(message: appRun, file: "${destDir}/app-run.bat")
    }

}

