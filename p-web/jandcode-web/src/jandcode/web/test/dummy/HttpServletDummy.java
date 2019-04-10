package jandcode.web.test.dummy;

import javax.servlet.*;
import javax.servlet.http.*;

public class HttpServletDummy extends HttpServlet {

    private ServletConfigDummy servletConfig = new ServletConfigDummy();

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void setContextPath(String contextPath) {
        servletConfig.getServletContext().setContextPath(contextPath);
    }

}
