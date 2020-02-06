package jandcode.core.apex.launcher;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.undertow.*;
import org.slf4j.*;

//todo ApexLauncher - сейчас примитивный. Cделать мультикомандным и расширяемым

/**
 * Запуск apex-приложения
 */
public class ApexLauncher {

    public void run(String[] args) throws Exception {
        UtLog.logOff();
        App app = AppLoader.load(AppConsts.FILE_APP_CONF);
        runWeb(app);
    }

    public void runWeb(App app) throws Exception {
        UndertowRunner r = new UndertowRunner();
        r.setPort(8080);
        r.setContext("/jc");

        System.out.println("start web-server: " + r.getUrl());

        reconfigureLog(app, "logback.xml");

        r.start(app);
    }

    public void reconfigureLog(App app, String logfile) {
        String f = UtFile.join(app.getAppdir(), "_" + logfile);
        if (!UtFile.exists(f)) {
            f = UtFile.join(app.getAppdir(), logfile);
        }
        if (!UtFile.exists(f)) {
            return;
        }
        //
        UtLog.logOn(f);
        LoggerFactory.getLogger(App.class).info("load log config from: " + f);
    }

}
