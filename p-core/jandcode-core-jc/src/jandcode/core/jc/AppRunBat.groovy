package jandcode.core.jc

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

/**
 * Генератор bat-файлов для запуска приложения
 */
class AppRunBat extends ProjectScript {

    protected void onInclude() throws Exception {
        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

        // idea
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)

        //
        addRunConfig("app-run", "")
    }

    //////

    /**
     * Класс с методом main, который запускает приложение
     */
    String mainClass = "jandcode.core.launcher.Launcher"

    /**
     * Шаблон файла bat
     */
    String templateBat = "core/app-run.bat.gsp"

    /**
     * Шаблон файла sh
     */
    String templateSh = "core/app-run.sh.gsp"

    /**
     * Конфигурации запуска
     */
    Map<String, String> runConfigs = [:]

    void prepareHandler() {
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll

        ant.mkdir(dir: wd(JcConsts.JC_METADATA_DIR))
        if (UtFile.isWindows()) {
            UtFile.saveString("${UtString.join(rlibs.classpathRaw, ";")}", new File(wd("_jc/app-run-classpath.txt")))
        } else {
            UtFile.saveString("CP=${UtString.join(rlibs.classpathRaw, ":")}", new File(wd("_jc/app-run-classpath.sh")))
        }

        generateBat(wd("app-run.bat"), false, false, null)
    }

    /**
     * Генерация bat
     * @param outFile куда. Расширение меняется в зависимости от параметра isWindows (bat, sh)
     * @param prod true - для режима prod, false - для режима dev
     * @param overwrite true - существующий файл перезаписывается
     * @param isWindows true - для windows, false - для linux, null - для текущей os
     */
    void generateBat(String outFile, boolean prod, boolean overwrite, Boolean isWindows = null) {
        isWindows = isWindows == null ? UtFile.isWindows() : isWindows

        outFile = UtFile.removeExt(outFile)
        outFile = outFile + (isWindows ? ".bat" : ".sh")

        if (!overwrite && UtFile.exists(outFile)) {
            return
        }

        String template = isWindows ? templateBat : templateSh
        template = include(FileResolver).getFile(template)

        GspScript g = create(template)
        g.generate(outFile, [
                mainClass: mainClass, prod: prod
        ])
    }

    /**
     * Добавить конфигурацию запуска для idea
     * @param name имя конфигурации
     * @param cliArgs аргументы командной строки
     */
    void addRunConfig(String name, String cliArgs) {
        this.runConfigs[name] = cliArgs
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        //
        for (a in runConfigs) {
            x.addRunConfig_main(a.key, this.mainClass, a.value, wd(""),
                    [
                            "-Djandcode.app.appdir=" + wd(""),
                            "-Dfile.encoding=UTF-8",
                    ],
                    [:]
            )
        }
    }

}
