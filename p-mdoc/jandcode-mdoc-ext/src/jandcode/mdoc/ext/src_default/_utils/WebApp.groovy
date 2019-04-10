package jandcode.mdoc.ext.src_default._utils

import jandcode.core.*
import jandcode.mdoc.cm.*
import jandcode.mdoc.source.*

/**
 * Генерация в контексте web-приложения
 */
class WebApp extends BaseCodeGen {

    App getApp(String appConfFile) {
        App app = cacheThis[appConfFile]
        if (app == null) {
            app = AppLoader.load(appConfFile)
            cacheThis[appConfFile] = app
        }
        return app
    }

    /**
     * groovy из gsp
     *
     * @attr appConf app.cfx файл
     * @attr src gsp файл
     */
    void gsp2groovy() {
        SourceFile appConf = attrs.getSourceFile("appConf")
        SourceFile src = attrs.getSourceFile("src")
        App app = getApp(appConf.realPath)
        //
        String text = app.bean("jandcode.web.WebService").getGspClassText(src.realPath)
        this.ext = "groovy"
        outText(text)
    }

    /**
     * сгенеренный из gsp текст.
     * Все атрибуты попадают в аргументы gsp
     *
     * @attr appConf app.cfx файл
     * @attr src gsp файл
     */
    void gsp2out() {
        SourceFile appConf = attrs.getSourceFile("appConf")
        SourceFile src = attrs.getSourceFile("src")
        App app = getApp(appConf.realPath)
        //
        def gctx = app.bean("jandcode.web.WebService").createGspContext()
        def text = gctx.render(src.realPath, attrs).toString()
        outText(text)
    }


}
