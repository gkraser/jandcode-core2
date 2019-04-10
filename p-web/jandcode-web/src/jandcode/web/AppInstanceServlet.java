package jandcode.web;


import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.web.impl.*;
import org.slf4j.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * Сервлет, который работает в контексте ранее созданного экземпляра приложения.
 */
public class AppInstanceServlet extends HttpServlet implements IAppLink {

    static {
        UtError.addErrorConvertor(new ErrorConvertorServlet());
    }

    protected static Logger log = LoggerFactory.getLogger(AppInstanceServlet.class);

    private App app;

    /**
     * Ссылка на приложение
     */
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
        // настраиваем
        WebService webService = app.bean(WebService.class);
        webService.setHttpServlet(this);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // проверяем необходимость перегрузки приложения в отладочном режиме
            if (app.isDebug() && !app.isTest()) {
                CheckChangedResourceInfo info = app.bean(CheckChangedResourceService.class).checkChangedResource();
                if (info.isNeedRestartApp()) {
                    log.warn("Требуется перезагрузка приложения из-за серьезнах изменений в ресурсах");
                }
            }

            // делегирование запроса
            app.bean(WebService.class).handleRequest(request, response);

        } catch (Throwable e) {
            throw new XErrorWrap(e);
        }
    }

}