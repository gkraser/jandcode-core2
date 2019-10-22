package jandcode.core.apex.ajc

import jandcode.core.*
import jandcode.jc.*

/**
 * Поддержка App
 */
class ApexAppManager extends ProjectScript {

    private App _app

    /**
     * Возвращает ссылку на приложение.
     * При первом вызове загружает его.
     */
    App getApp() {
        if (_app == null) {
            String appCfx = wd("app.cfx")
            ut.stopwatch.start("load app")
            _app = AppLoader.load(appCfx)
            ut.stopwatch.stop("load app")
        }
        return _app
    }

}
