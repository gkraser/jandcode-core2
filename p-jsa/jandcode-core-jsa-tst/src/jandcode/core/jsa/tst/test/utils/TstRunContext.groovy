package jandcode.core.jsa.tst.test.utils

import jandcode.commons.*
import jandcode.core.*
import jandcode.core.web.*
import jandcode.core.web.gsp.*
import jandcode.core.web.virtfile.*

import java.util.regex.*

/**
 * Контекст запуска файла в _tst/run
 */
class TstRunContext implements IGspContextLinkSet {

    /**
     * Файл, который запускается
     */
    String main

    /**
     * Файл темы
     */
    String theme

    /**
     * Файл среды
     */
    String env

    /**
     * Доступные темы
     */
    List<String> themes = []

    /**
     * Рекомендуемый заголовок страницы
     */
    String title

    /**
     * В каком модуле запускаемый файл.
     * Тееоретически может быть null.
     */
    Module module

    /**
     * Конфигурация для передачи в gsp
     */
    Map getCfg() {
        Map cfg = [:]
        cfg.path = main
        cfg.fileName = UtFile.filename(main)
        cfg.filePath = UtFile.path(main)

        cfg.theme = theme

        cfg.themes = []
        for (item in themes) {
            cfg.themes.add([path: item])
        }

        return cfg
    }

    //////

    GspContext gspCtx

    void setGspContext(GspContext gspContext) {
        this.gspCtx = gspContext
        init()
    }

    private void init() {
        Gsp gsp = gspCtx.rootGsp
        WebService svc = gsp.webService

        main = gsp.args.getString("path")
        main = UtVDir.normalize(main)

        def tstUt = gspCtx.inst(JsaTstUtils)

        // module
        module = UtWeb.findModuleForPath(gsp.app, main)

        // all themes
        themes = UtWeb.expandPath(gsp.app, "[*]/css/theme-*.js")
        def themesByName = [:]
        Pattern patternTheme = Pattern.compile(".*/css/theme-(.*).js");
        for (t in themes) {
            Matcher m = patternTheme.matcher(t);
            if (m.find()) {
                themesByName[m.group(1)] = t
            }
        }
        if (module) {
            themes.addAll(UtWeb.expandPath(gsp.app, "${module.getVPath()}/_tst/**/theme-*.js"))
        }
        // theme
        if (gsp.request.params.theme) {
            String thName = gsp.request.params.theme
            def ft = themesByName[thName]
            if (ft != null) {
                theme = ft
            } else {
                def ft1 = svc.findFile(thName)
                if (ft1 != null) {
                    theme = ft1.path
                }
            }
        }
        if (!theme) {
            // локальная тема для тестов
            theme = tstUt.findFileUp(main, "theme-tst.js")
        }
        if (!theme) {
            // если есть тема app, используем ее по умолчанинию
            theme = themesByName['app']
        }
        if (!theme) {
            // темы не найдены, используем стандартную
            theme = "jandcode/core/jsa/core/css/theme-std.js"
        }

        // общая среда
        env = tstUt.findFileUp(main, "env-tst.js")
        if (!env && module) {
            VirtFile f = svc.findFile("${module.getVPath()}/index.js")
            if (f == null) {
                f = svc.findFile("${module.getVPath()}/js/index.js")
            }
            if (f != null) {
                env = f.path
            }
        }
        if (!env) {
            env = "jandcode/core/jsa/core/index.js"
        }

        //
        title = UtFile.filename(main) + " - " + main
    }

}
