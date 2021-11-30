package jandcode.core.jc

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Сборщик продукта для приложения.
 */
class AppProductBuilder extends ProductBuilder {

    /**
     * Событие возникает как этап сборки продукта в onExec
     */
    static class Event_Exec extends BaseJcEvent {

        /**
         * Экземпляр сборщика
         */
        AppProductBuilder builder

        Event_Exec(AppProductBuilder builder) {
            this.builder = builder
        }

    }

    /**
     * Событие возникает как последний этап сборки продукта в onExec,
     * после того, как отработало событие {@link jandcode.core.jc.AppProductBuilder.Event_Exec}.
     */
    static class Event_AfterExec extends BaseJcEvent {

        /**
         * Экземпляр сборщика
         */
        AppProductBuilder builder

        Event_AfterExec(AppProductBuilder builder) {
            this.builder = builder
        }

    }

    /**
     * Дополнительные библиотеки для включения в product
     */
    List includeLibs = []

    /**
     * Маски имен модулей, которые не попадют в product при сборке в режиме prod.
     * Если сборка в режиме debug, то этот список не используется.
     */
    List<String> ignoreModules = []

    boolean isIgnoreModule(String name) {
        for (String mask in ignoreModules) {
            if (UtVDir.matchPath(mask, name)) {
                return true
            }
        }
        return false
    }

    void onExec() {
        // собираем проект
        buildProject()

        // копируем шаблон содержимого каталога продукта из проекта
        copyFolderToDestDir("data/product")

        // копируем библиотеки в libs
        def cp = createLibCopier()

        for (Project p in include(RootProject).modules) {
            if (p.getIncluded(JavaProject) != null) {
                if (!isIgnoreModule(p.name) || ctx.env.debug) {
                    cp.add(p.name)
                }
            }
        }

        cp.add(include(RootProject).depends.prod.names)
        if (ctx.env.debug) {
            cp.add(include(RootProject).depends.dev.names)
        }
        cp.add(includeLibs)
        cp.copyTo("${destDir}/lib")

        // app
        ant.copy(file: wd("app.cfx"), todir: "${destDir}")
        if (ctx.env.debug) {
            String fnAppDev = wd("app-dev.cfx")
            if (UtFile.exists(fnAppDev)) {
                ant.copy(file: fnAppDev, todir: "${destDir}")
            }
        }

        // bat
        makeRunBat()

        // version
        makeVersionFile()

        if (ctx.env.debug) {
            ant.echo(message: "jandcode.env.dev=true\n", file: "${destDir}/.env")
        }

        // уведомляем
        fireEvent(new Event_Exec(this))
        fireEvent(new Event_AfterExec(this))
    }

    void makeRunBat() {
        AppRunBat rb = include(AppRunBat)
        //
        rb.generateBat("${destDir}/app-run.bat", true, true, true)
        rb.generateBat("${destDir}/app-run.sh", true, true, false)
    }


}
