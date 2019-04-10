package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Базовый простой сборщик продукта для lib-проекта.
 * Можно использовать напрямую или как шаблон для создания собственного.
 */
class LibProductBuilder extends ProductBuilder {

    /**
     * Маски имен модулей, которые не попадют в product
     */
    List<String> ignoreModules = ["sandbox*", "*-test", "*-tests", "*-docs"]

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
        cp.createLibXml = true
        for (Project p in include(RootProject).modules) {
            if (p.getIncluded(JavaProject) != null) {
                if (!isIgnoreModule(p.name)) {
                    cp.add(p.name, false)
                }
            }
        }
        if (UtFile.exists(wd("lib"))) {
            def libs = ctx.loadLibs(wd("lib"))
            cp.add(libs, false)
        }
        cp.copyTo("${destDir}/lib", true, true)

        // project.jc
        UtFile.saveString("", new File("${destDir}/project.jc"))

        // version
        makeVersionFile()

        // .jc-root
        makeJcRootFile()
    }

}
