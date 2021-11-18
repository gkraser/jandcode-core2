package jandcode.core.web.test;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;

import java.util.*;

public class WebServerHolder {

    /**
     * Путь в конфигурации приложения, где хранятся настройки и данные webserver
     */
    public static final String APP_CONF_WEBSERVER = "test/webserver";

    /**
     * Путь в конфигурации приложения.
     * Контекст, на котором будет запускаться webserver, по умолчанию /test1
     */
    public static final String APP_CONF_WEBSERVER_CONTEXT = APP_CONF_WEBSERVER + "/context";

    /**
     * Путь в конфигурации приложения.
     * Сервер с каим id запущен для этого приложения.
     */
    public static final String APP_CONF_WEBSERVER_ID = APP_CONF_WEBSERVER + "/webserver-id";

    private String contextDefault = "/test1";
    private int nextPort = 3366;
    private Map<String, WebServer> servers = new HashMap<>();

    public WebServer getWebServer(App app) {
        String context = app.getConf().getString(APP_CONF_WEBSERVER_CONTEXT);
        if (UtString.empty(context)) {
            context = contextDefault;
        }

        // есть ли у приложения webserver
        String id = app.getConf().getString(APP_CONF_WEBSERVER_ID);

        WebServer ws = servers.get(id);

        if (ws == null) {
            ws = createWebServer(app, context);
            try {
                System.out.println("***** [start websever: " + ws.getUrl() +
                        " for app: " + UtFile.vfsPathToLocalPath(app.getAppConfFile()) +
                        " ]***************");
                ws.start();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
            app.getConf().setValue(APP_CONF_WEBSERVER_ID, ws.getId());
            this.servers.put(ws.getId(), ws);
        }

        return ws;
    }

    private WebServer createWebServer(App app, String context) {
        nextPort++;
        String id = "__id__" + nextPort;
        return new WebServer(id, app, nextPort, context);
    }

}
