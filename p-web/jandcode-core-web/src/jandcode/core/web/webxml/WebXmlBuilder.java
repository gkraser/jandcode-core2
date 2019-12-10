package jandcode.core.web.webxml;

import jandcode.commons.*;

/**
 * Простой утилитный построитель WebXml
 */
public class WebXmlBuilder {

    private WebXml webXml;

    public WebXmlBuilder(WebXml webXml) {
        this.webXml = webXml;
    }

    public WebXml getWebXml() {
        return webXml;
    }

    //////

    /**
     * Добавить сервлет
     *
     * @param servletName   имя сервета
     * @param servletClass  класс сервлета
     * @param urlPattern    если не пустой - servlet-mapping/url-pattern
     * @param loadOnStartup load-on-startup
     */
    public WebXml.Servlet addServlet(String servletName, String servletClass, String urlPattern, int loadOnStartup) {
        WebXml.Servlet sv = new WebXml.Servlet();
        sv.setServletName(servletName);
        sv.setServletClass(servletClass);
        sv.setLoadOnStartup(loadOnStartup);
        addMapping(sv, urlPattern);
        getWebXml().getServlets().add(sv);
        return sv;
    }

    public void addMapping(WebXml.Servlet sv, String urlPattern) {
        if (!UtString.empty(urlPattern)) {
            WebXml.ServletMapping m = new WebXml.ServletMapping();
            m.setServletName(sv.getServletName());
            m.setUrlPattern(urlPattern);
            getWebXml().getServletMappings().add(m);
        }
    }

    public void addInitParam(WebXml.Servlet sv, String paramName, String paramValue) {
        if (!UtString.empty(paramName)) {
            WebXml.InitParam p = new WebXml.InitParam();
            p.setParamName(paramName);
            p.setParamValue(paramValue);
            sv.getInitParams().add(p);
        }
    }

    public void setMultipartConfig(WebXml.Servlet sv) {
        WebXml.MultipartConfig mc = sv.getMultipartConfig();
        if (mc == null) {
            mc = new WebXml.MultipartConfig();
            sv.setMultipartConfig(mc);
        }
    }

    private void addErrorPage(String location, int errorCode, String exceptionType) {
        WebXml.ErrorPage p = new WebXml.ErrorPage();
        p.setLocation(location);
        if (errorCode != 0) {
            p.setErrorCode(errorCode);
        }
        if (!UtString.empty(exceptionType)) {
            p.setExceptionType(exceptionType);
        }
        getWebXml().getErrorPages().add(p);
    }

    public void addErrorPage(String location, int errorCode) {
        addErrorPage(location, errorCode, null);
    }

    public void addErrorPage(String location, String exceptionType) {
        addErrorPage(location, 0, exceptionType);
    }

    public void addErrorPage(String location) {
        addErrorPage(location, 0, null);
    }

}
