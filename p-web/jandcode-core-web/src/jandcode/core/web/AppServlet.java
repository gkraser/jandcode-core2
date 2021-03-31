package jandcode.core.web;


import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.core.web.impl.*;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * Сервлет приложения.
 * Может как сам создавать приложение, так работать с созданным извне.
 * <p>
 * Создает приложение сам, когда запускается во внешнем контейнере сервлетов,
 * например tomcat.
 * <p>
 * Полностью управляет приложением, которое ему передали.
 * В том числе делает ему startup/shutdow, а так же может перезагрузить
 * приложение в dev-режиме. Вообщем приложение, попавшее сервлет, извне
 * должно стать неиспользуемым.
 */
public class AppServlet extends HttpServlet implements IAppLink {

    static {
        UtError.addErrorConvertor(new ErrorConvertorServlet());
    }

    protected static Logger log = LoggerFactory.getLogger(AppServlet.class);

    /**
     * Настройка логирования.
     * Значение: boolean. По умолчанию - true (логирование включено).
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

    public AppServlet() {
    }

    /**
     * Создать сервлет для уже существующего приложения
     */
    public AppServlet(App app) {
        this.app = app;
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            if (app == null) {
                // если приложение явно не установлено, грузим его сами
                initApp();
            } else {
                useApp(app);
            }
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

        boolean envTest = false;

        // уведомляем о конце
        if (app != null) {
            envTest = app.getEnv().isTest();
            try {
                app.shutdown();
            } catch (Exception e) {
                log.error("Error app shutdown", e);
            }
            app = null;
        }

        // VFS reset, иначе файлы jar не закрываются и при deploy на tomcat
        // старое приложение не удаляется
        if (!envTest) {
            try {
                log.info("VFS.reset()");
                VFS.reset();
            } catch (FileSystemException e) {
                log.error("Error VFS.reset()", e);
            }
        }

        super.destroy();
    }

    /**
     * Ссылка на приложение сервлета.
     * В dev-режиме это может быть не изначальный экземпляр, а перезагруженный.
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

        // определяем каталог приложения
        String appDir = UtEnv.resolveAppdir(baseDir);

        // менеджер логов
        AppLogManager logManager = new AppLogManager(appDir);

        // настраиваем логирование
        s = getInitParameter(initParameter_log);
        if (UtCnv.toBoolean(s, true)) {
            logManager.logOn();
        }

        // ищем конфиг
        String appConfFile = getInitParameter(initParameter_app);
        if (UtString.empty(appConfFile)) {
            appConfFile = AppConsts.FILE_APP_CONF;
        }
        appConfFile = UtFile.join(appDir, appConfFile);

        // загружаем приложение
        app = loadApp(appConfFile, appDir);
    }

    protected App loadApp(String appConfFile, String appDir) throws Exception {
        // загружаем приложение
        App app = AppLoader.load(appConfFile, appDir, null, false);

        useApp(app);

        return app;
    }

    protected void useApp(App app) {
        // настраиваем webService
        WebService webService = app.bean(WebService.class);

        // настраиваем его
        webService.setHttpServlet(this);

        // уведомляем о старте
        try {
            app.startup();
        } catch (Exception e) {
            try {
                app.shutdown();
            } catch (Exception e1) {
                // ignore
            }
            throw e;
        }
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
            App tempApp = loadApp(app.getAppConfFile(), app.getAppdir());

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