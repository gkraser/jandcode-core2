package jandcode.core.web.undertow;

import io.undertow.*;
import io.undertow.server.*;
import io.undertow.server.handlers.*;
import io.undertow.servlet.*;
import io.undertow.servlet.api.*;
import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.webxml.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Запускалка undertow-сервера
 */
public class UndertowRunner {

    private int port = 8080;
    private String context = "/jc";
    private String listenerHost = "0.0.0.0";

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
     * Запуск
     */
    public void start() throws Exception {
        startWebXml(new DefaultWebXmlFactory().createWebXml());
    }

    /**
     * Запуск с указанным экземпляром приложения
     */
    public void start(App app) throws Exception {
        WebXml wx = new DefaultWebXmlFactory().createWebXml();
        AppServlet svInst = new AppServlet(app);
        wx.getServlet(WebConsts.WEB_SERVLET_NAME).setServletInstance(svInst);
        startWebXml(wx);
    }

    @SuppressWarnings("unchecked")
    public void startWebXml(WebXml wx) throws Exception {

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
        DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        manager.deploy();
        HttpHandler h = manager.start();

        PathHandler path1 = Handlers.path()
                .addPrefixPath(this.context, h);

        Undertow server = Undertow.builder()
                .addHttpListener(this.port, this.listenerHost)
                .setHandler(path1)
                .build();
        server.start();
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
