package jandcode.core.jc


import jandcode.jc.std.*

/**
 * Сборщик продукта для приложения.
 */
class AppProductBuilder extends ProductBuilder {

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
    }

    void makeRunBat() {
        AppRunBat rb = include(AppRunBat)
        //
        rb.generateBat("${destDir}/app-run.bat", true, true, true)
        rb.generateBat("${destDir}/app-run.sh", true, true, false)
    }


}
