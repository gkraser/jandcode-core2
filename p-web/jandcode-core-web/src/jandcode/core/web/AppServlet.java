package jandcode.core.web;


import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;
import jandcode.commons.stopwatch.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.core.web.impl.*;
import org.slf4j.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * Сервлет.
 */
public class AppServlet extends HttpServlet implements IAppLink {

    static {
        UtError.addErrorConvertor(new ErrorConvertorServlet());
    }

    protected static Logger log = LoggerFactory.getLogger(AppServlet.class);

    /**
     * Настройка логирования
     */
    public static final String initParameter_log = "log";

    /**
     * Имя файла с приложением. Если не указано, используется 'app.cfx' в качестве
     * значения.
     */
    public static final String initParameter_app = "app";

    /**
     * Имя атрибута контекста сервлета, в котором будет зарегистрирована
     * ссылка на сервлет для межсервлетного взаимодействия.
     */
    public static final String initParameter_servletref = "servlet.ref";

    protected App app;
    protected boolean reloadAppWork;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            initApp();
        } catch (Exception e) {
            showError(e, true);
            throw new ServletException(e);
        }

        // регистрируем сервлет в атрибутах контекста, если заказано
        String servletRefAttrName = getInitParameter(initParameter_servletref);
        if (!UtString.empty(servletRefAttrName)) {
            getServletContext().setAttribute(servletRefAttrName, this);
        }

    }

    public void destroy() {
        // убираем сервлет из атрибутов контекста, если он там был
        String servletRefAttrName = getInitParameter(initParameter_servletref);
        if (!UtString.empty(servletRefAttrName)) {
            getServletContext().removeAttribute(servletRefAttrName);
        }

        // уведомляем о конце
        if (app != null) {
            try {
                app.shutdown();
            } catch (Exception e) {
                log.error("Error app shutdown", e);
            }
            app = null;
        }
        super.destroy();
    }

    /**
     * Ссылка на приложение сервлета
     */
    public App getApp() {
        return app;
    }

    protected void initApp() throws Exception {
        String s;

        // отключаем логирование
        UtLog.logOff();

        String baseDir = getServletContext().getRealPath("/WEB-INF");
        if (UtString.empty(baseDir)) {
            baseDir = UtFile.getWorkdir();
        }

        // настраиваем логирование
        s = getInitParameter(initParameter_log);
        if (!UtString.empty(s)) {
            String p = UtFile.path(s);
            if (UtString.empty(p)) {
                s = UtFile.findFileUp(s, baseDir);
            }
            if (s != null) {
                if (!UtFile.exists(s)) {
                    s = null;
                }
            }
            UtLog.logOn(s);
            if (s != null) {
                log.info("load log config from: " + s);
            }
        }

        // ищем конфиг
        String appConfFile = getInitParameter(initParameter_app);
        if (UtString.empty(appConfFile)) {
            appConfFile = AppConsts.FILE_APP_CONF;
        }
        appConfFile = UtFile.join(baseDir, appConfFile);

        // загружаем приложение
        app = loadApp(appConfFile);
    }

    protected App loadApp(String appConfFile) throws Exception {
        Stopwatch sw = new DefaultStopwatch("load app");
        sw.start();

        // загружаем приложение
        log.info("load app from: " + appConfFile);
        App app = AppLoader.load(appConfFile);
        sw.stop();
        log.info("appdir: " + app.getAppdir());
        if (app.getEnv().isDev()) {
            log.info("app.env.dev=true");
        }
        log.info(sw.toString());

        // настраиваем webService
        WebService webService = app.bean(WebService.class);

        // настраиваем его
        webService.setHttpServlet(this);

        // уведомляем о старте
        app.startup();

        return app;
    }

    /**
     * Перезагрузить приложение.
     * Используется в отладочном режиме при изменении ресурсов conf
     */
    protected void reloadApp() throws Exception {
        if (reloadAppWork) {
            return;
        }
        reloadAppWork = true;
        try {
            // загружаем до убивания старого приложения
            // что бы если новое не загрузилось, старое не перестало работать
            App tempApp = loadApp(app.getAppConfFile());

            // уведомляем о убивании приложения
            try {
                app.shutdown();
            } catch (Exception e) {
                showError(e, false);
            }

            // меняем приложение
            app = tempApp;
        } catch (Exception e) {
            throw new XErrorMark(e, "reloadApp");
        } finally {
            reloadAppWork = false;
        }
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // проверяем необходимость перегрузки приложения в отладочном режиме
            if (app.getEnv().isDev() && !app.getEnv().isTest() && !reloadAppWork) {
                CheckChangedResourceInfo info = app.bean(CheckChangedResourceService.class).checkChangedResource();
                if (info.isNeedRestartApp()) {
                    try {
                        reloadApp();
                    } catch (Exception e) {
                        showError(e, false);
                        throw e;
                    }
                }
            }

            // делегирование запроса
            app.bean(WebService.class).handleRequest(request, response);

        } catch (Throwable e) {
            throw new XErrorWrap(e);
        }
    }

    protected void showError(Exception e, boolean fullStack) {
        String msg = new ErrorFormatterDefault(true, true, fullStack).
                getMessage(UtError.createErrorInfo(e));
        if (log.isErrorEnabled()) {
            log.error("\n" + msg);
        } else {
            System.err.println(msg);
        }
    }

}