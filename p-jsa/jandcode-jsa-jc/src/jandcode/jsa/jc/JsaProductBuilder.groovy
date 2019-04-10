package jandcode.jsa.jc

import jandcode.jc.std.*

/**
 * Базовый простой сборщик продукта для jsa-приложения.
 * Можно использовать напрямую или как шаблон для создания собственного.
 */
class JsaProductBuilder extends ProductBuilder {
    void onExec() {
        // собираем проект
        buildProject()

        // копируем встроенный шаблон содержимого каталога продукта
        copyFolderToDestDir(ctx.service(JcDataService).getFile("product/jsa"))

        // копируем шаблон содержимого каталога продукта из проекта
        copyFolderToDestDir("data/product")

        // копируем библиотеки в libs
        def cp = createLibCopier()
        cp.add(include(RootProject).modules)
        cp.add("jandcode-undertow")
        cp.copyTo("${destDir}/lib")

        // дополнительные сгенерированные jar
        ant.copy(file: getIncluded(JsaRootProject).getFileJsaWebrootJar(), todir: "${destDir}/lib")

        // app
        ant.copy(file: wd("app.cfx"), todir: "${destDir}")
        ant.echo(message: 'jandcode.project.root=${path}', file: "${destDir}/.pathprop")

        // version
        makeVersionFile()

        // .jc-root
        makeJcRootFile()
    }

}
