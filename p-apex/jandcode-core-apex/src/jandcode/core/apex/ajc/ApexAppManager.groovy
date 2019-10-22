package jandcode.core.apex.ajc

import jandcode.commons.*
import jandcode.core.*
import jandcode.jc.*
import org.slf4j.*

/**
 * Поддержка App
 */
class ApexAppManager extends ProjectScript {

    String logFile = "logback.xml"

    /**
     * Возвращает ссылку на приложение.
     * При первом вызове загружает его.
     */
    App getApp() {
        if (_app == null) {
            String appCfx = wd("app.cfx")
            log.info("load app from [${appCfx}]")
            ut.stopwatch.start("load app")
            _app = AppLoader.load(appCfx)
            ut.stopwatch.stop("load app")
        }
        return _app
    }

    private App _app

    /**
     * Переконфигурация логирования.
     * Вызывается при фактическом запуске приложения.
     * Конфигурирует из файлов _logback.xml (приоритет) или из logback.xml.
     */
    void reconfigureLog() {
        String f = wd("_${logFile}")
        if (!UtFile.exists(f)) {
            f = wd("_${logFile}")
        }
        if (!UtFile.exists(f)) {
            return
        }
        //
        UtLog.logOn(f);
        LoggerFactory.getLogger(App.class).info("load log config from: " + f);
    }

}
