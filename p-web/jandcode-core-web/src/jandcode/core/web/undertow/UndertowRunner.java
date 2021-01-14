package jandcode.core.web.undertow;

import io.undertow.*;
import io.undertow.server.*;
import io.undertow.server.handlers.*;
import io.undertow.servlet.*;
import io.undertow.servlet.api.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.webxml.*;
import org.slf4j.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Запускалка undertow-сервера
 */
public class UndertowRunner {

    protected static Logger log = LoggerFactory.getLogger(UndertowRunner.class);

    private int port = 8080;
    private String context = "/jc";
    private String listenerHost = "0.0.0.0";

    private Undertow server;
    private DeploymentManager manager;

    ////// настройки

    public void setPort(int port) {
        this.port = port;
    }

    public void setContext(String context) {
        this.context = context;
        if (UtString.empty(this.context)) {
            this.context = "/";
        } else if (!this.context.startsWith("/")) {
            this.context = "/" + this.context;
        }
    }

    public void setListenerHost(String listenerHost) {
        this.listenerHost = listenerHost;
        if (UtString.empty(this.listenerHost)) {
            this.listenerHost = "0.0.0.0";
        }
    }

    public String getUrl() {
        return "http://localhost:" + port + context;
    }

    ////// запускалки

    /**
     * Запуск сервера с настройками WebXml по умолчанию.
     */
    public void start() throws Exception {
        start(new DefaultWebXmlFactory().createWebXml());
    }

    /**
     * Запуск сервера с указанным экземпляром приложения
     */
    public void start(App app) throws Exception {
        WebXml wx = new DefaultWebXmlFactory().createWebXml();
        AppServlet svInst = new AppServlet(app);
        wx.getServlet(WebConsts.WEB_SERVLET_NAME).setServletInstance(svInst);
        start(wx);
    }

    protected void checkNotStarted() {
        if (this.server != null) {
            throw new XError("undertow server already started");
        }
    }

    /**
     * Остановить сервер
     */
    public void stop() {
        if (this.server == null) {
            return;
        }
        try {
            this.manager.stop();
        } catch (Exception e) {
            log.error("manager stop error", e);
        }
        try {
            this.manager.undeploy();
        } catch (Exception e) {
            log.error("manager undeploy error", e);
        }
        this.server.stop();
        this.server = null;
    }

    /**
     * Запущен ли сервер
     */
    public boolean isStarted() {
        return this.server != null;
    }

    /**
     * Запустить сервер по настройкам в WebXml
     */
    @SuppressWarnings("unchecked")
    public void start(WebXml wx) throws Exception {

        checkNotStarted();

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(UtClass.getClassLoader())
                .setContextPath(this.context)
                .setDeploymentName("jandcode-core-web-app.war");

        for (WebXml.Servlet sv : wx.getServlets()) {
            ServletInfo servletInfo = null;
            if (sv.getServletInstance() != null) {
                servletInfo = Servlets.servlet(sv.getServletName(),
                        sv.getServletInstance().getClass(),
                        new InstanceServletFactory(sv.getServletInstance()));
            } else {
                servletInfo = Servlets.servlet(sv.getServletName(),
                        UtClass.getClass(sv.getServletClass()));
            }
            servletInfo.setLoadOnStartup(sv.getLoadOnStartup());
            for (WebXml.InitParam p : sv.getInitParams()) {
                servletInfo.addInitParam(p.getParamName(), p.getParamValue());
            }
            for (WebXml.ServletMapping m : wx.getServletMappings()) {
                if (m.getServletName() == null) {
                    continue;
                }
                if (m.getServletName().equals(sv.getServletName())) {
                    servletInfo.addMapping(m.getUrlPattern());
                }
            }

            WebXml.MultipartConfig mpc = sv.getMultipartConfig();
            if (mpc != null) {
                servletInfo.setMultipartConfig(new MultipartConfigElement(
                        mpc.getLocation(),
                        mpc.getMaxFileSize(),
                        mpc.getMaxRequestSize(),
                        mpc.getFileSizeThreshold()
                ));
            }

            servletBuilder.addServlet(servletInfo);
        }

        for (WebXml.ErrorPage ep : wx.getErrorPages()) {
            ErrorPage errorPage;
            if (ep.getErrorCode() == 0 && UtString.empty(ep.getExceptionType())) {
                errorPage = new ErrorPage(ep.getLocation());
            } else if (ep.getErrorCode() != 0) {
                errorPage = new ErrorPage(ep.getLocation(), ep.getErrorCode());
            } else {
                errorPage = new ErrorPage(ep.getLocation(), UtClass.getClass(ep.getExceptionType()));
            }
            servletBuilder.addErrorPage(errorPage);
        }

        //////

        startServer(servletBuilder);
    }

    ////// internal

    protected void startServer(DeploymentInfo deploymentInfo) throws Exception {
        DeploymentManager manager = Servlets.newContainer().addDeployment(deploymentInfo);
        manager.deploy();
        HttpHandler h = manager.start();

        PathHandler path1 = Handlers.path()
                .addPrefixPath(this.context, h);

        Undertow server = Undertow.builder()
                .addHttpListener(this.port, this.listenerHost)
                .setHandler(path1)
                .build();
        server.start();
        //
        this.server = server;
        this.manager = manager;
    }


    ////// undertow instance servlet factory

    class InstanceServletFactory implements InstanceFactory<HttpServlet> {

        private HttpServlet inst;

        public InstanceServletFactory(HttpServlet inst) {
            this.inst = inst;
        }

        public InstanceHandle<HttpServlet> createInstance() throws InstantiationException {
            return new InstanceHandle<HttpServlet>() {
                public HttpServlet getInstance() {
                    return inst;
                }

                public void release() {
                }
            };
        }
    }

}
