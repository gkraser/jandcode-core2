package _utils

import jandcode.mdoc.cm.*
import jandcode.mdoc.source.*
import jandcode.core.*

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
        String text = app.bean("jandcode.xweb.WebService").getGspClassText(src.realPath)
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
        def gctx = app.bean("jandcode.xweb.WebService").createGspContext()
        def text = gctx.render(src.realPath, attrs).toString()
        outText(text)
    }


}
