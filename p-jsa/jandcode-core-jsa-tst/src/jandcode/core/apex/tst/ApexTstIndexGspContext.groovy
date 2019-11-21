package jandcode.core.apex.tst

import jandcode.commons.*
import jandcode.core.jsa.gsp.*
import jandcode.core.jsa.theme.*
import jandcode.core.web.*
import jandcode.core.web.gsp.*
import jandcode.core.web.virtfile.*

/**
 * Сервисные методы для использования в index.gsp и аналогичных файлах,
 * которые формируют стартовый html-файл приложения для выполнения _tst/run.
 */
class ApexTstIndexGspContext implements IGspContextLinkSet {

    private GspContext gspContext
    private String title
    private Map cfg

    void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
    }

    /**
     * Конфигурация для передачи в gsp
     */
    Map getCfg() {
        if (cfg == null) {
            cfg = [:]

            JsaIndexGspContext ctx = gspContext.inst(JsaIndexGspContext)

            String main = ctx.main

            cfg.path = main
            cfg.fileName = UtFile.filename(main)
            cfg.filePath = UtFile.path(main)
            cfg.fileNameBase = UtFile.removeExt(UtFile.removeExt(cfg.fileName))

            cfg.theme = ctx.theme
            cfg.themes = ctx.themesCfg

        }
        return cfg
    }

    /**
     * Конфигурация для клиента в виде json-строки
     */
    String getCfgJson() {
        return UtJson.toJson(getCfg())
    }

    String getTitle() {
        if (UtString.empty(title)) {
            JsaIndexGspContext ctx = gspContext.inst(JsaIndexGspContext)
            String main = ctx.main
            return UtFile.filename(main) + " - " + main
        }
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    /**
     * Возвращает полное имя модуля env-tst.js начиная с каталога теста.
     * Если такого файла нет, возвращается null
     */
    String getEnvTstJs() {
        JsaIndexGspContext ctx = gspContext.inst(JsaIndexGspContext)
        String main = ctx.main

        WebService webSvc = gspContext.getApp().bean(WebService)

        String template = "env-tst.js"
        String cp = UtFile.path(main)
        while (!UtString.empty(cp)) {
            String fn = UtVDir.join(cp, template)
            VirtFile ft = webSvc.findFile(fn)
            if (ft != null) {
                return ft.getPath()
            }
            cp = UtFile.path(cp)
        }
        return null

    }

}
