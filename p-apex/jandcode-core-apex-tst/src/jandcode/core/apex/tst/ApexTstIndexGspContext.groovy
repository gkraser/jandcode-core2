package jandcode.core.apex.tst

import jandcode.commons.*
import jandcode.core.jsa.gsp.*
import jandcode.core.jsa.theme.*
import jandcode.core.web.gsp.*

/**
 * Сервисные методы для использования в index.gsp и аналогичных файлах,
 * которые формируют стартовый html-файл приложения для выполнения _tst/run.
 */
class ApexTstIndexGspContext implements IGspContextLinkSet {

    private GspContext gspContext

    void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
    }

    /**
     * Конфигурация для передачи в gsp
     */
    Map getCfg() {
        JsaIndexGspContext ctx = gspContext.inst(JsaIndexGspContext)
        JsaThemeService themeSvc = gspContext.getApp().bean(JsaThemeService)

        String main = ctx.main

        Map cfg = [:]
        cfg.path = main
        cfg.fileName = UtFile.filename(main)
        cfg.filePath = UtFile.path(main)

        cfg.theme = ctx.theme

        cfg.themes = []
        for (themeName in themeSvc.getThemeNames()) {
            cfg.themes.add([name: themeName, path: themeSvc.findThemeFile(themeName)])
        }

        return cfg
    }

    /**
     * Конфигурация для клиента в виде json-строки
     */
    String getCfgJson() {
        return UtJson.toJson(getCfg())
    }

}
