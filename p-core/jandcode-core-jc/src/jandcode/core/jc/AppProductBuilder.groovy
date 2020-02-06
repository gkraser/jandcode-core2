package jandcode.core.jc

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

    void onExec() {
        // собираем проект
        buildProject()

        // копируем шаблон содержимого каталога продукта из проекта
        copyFolderToDestDir("data/product")

        // копируем библиотеки в libs
        def cp = createLibCopier()
        cp.add(include(RootProject).modules)
        cp.add(include(RootProject).depends.prod.names)
        cp.add(includeLibs)
        cp.copyTo("${destDir}/lib")

        // app
        ant.copy(file: wd("app.cfx"), todir: "${destDir}")

        // bat
        makeRunBat()

        // version
        makeVersionFile()

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
