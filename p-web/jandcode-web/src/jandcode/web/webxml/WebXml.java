package jandcode.web.webxml;

import jandcode.commons.error.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * Частичное представление web.xml, расширено некоторыми специфическими
 * для jandcode вещами.
 */
public class WebXml {

    private List<Servlet> servlets = new ArrayList<>();
    private List<ServletMapping> servletMappings = new ArrayList<>();
    private List<ErrorPage> errorPages = new ArrayList<>();

    //////

    public static class Servlet {
        private String servletName;
        private String servletClass;
        private List<InitParam> initParams = new ArrayList<>();
        private int loadOnStartup;
        private HttpServlet servletInstance;

        public String getServletName() {
            return servletName;
        }

        public void setServletName(String servletName) {
            this.servletName = servletName;
        }

        public String getServletClass() {
            return servletClass;
        }

        public void setServletClass(String servletClass) {
            this.servletClass = servletClass;
        }

        public List<InitParam> getInitParams() {
            return initParams;
        }

        public int getLoadOnStartup() {
            return loadOnStartup;
        }

        public void setLoadOnStartup(int loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }

        /**
         * Явно созданный экземпляр сервлета
         */
        public HttpServlet getServletInstance() {
            return servletInstance;
        }

        public void setServletInstance(HttpServlet servletInstance) {
            this.servletInstance = servletInstance;
        }
    }

    //////

    public static class InitParam {
        private String paramName;
        private String paramValue;

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getParamValue() {
            return paramValue;
        }

        public void setParamValue(String paramValue) {
            this.paramValue = paramValue;
        }
    }

    //////

    public static class ServletMapping {
        private String servletName;
        private String urlPattern;

        public String getServletName() {
            return servletName;
        }

        public void setServletName(String servletName) {
            this.servletName = servletName;
        }

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }
    }

    //////

    public static class ErrorPage {
        private String location;
        private int errorCode;
        private String exceptionType;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getExceptionType() {
            return exceptionType;
        }

        public void setExceptionType(String exceptionType) {
            this.exceptionType = exceptionType;
        }
    }

    //////

    public List<Servlet> getServlets() {
        return servlets;
    }

    public Servlet findServlet(String servletName) {
        if (servletName == null) {
            return null;
        }
        for (Servlet sv : this.servlets) {
            if (servletName.equals(sv.getServletName())) {
                return sv;
            }
        }
        return null;
    }

    public Servlet getServlet(String servletName) {
        Servlet sv = findServlet(servletName);
        if (sv == null) {
            throw new XError("Servlet [{0}] not found", servletName);
        }
        return sv;
    }

    //////

    public List<ServletMapping> getServletMappings() {
        return servletMappings;
    }

    //////

    public List<ErrorPage> getErrorPages() {
        return errorPages;
    }

    //////


}
