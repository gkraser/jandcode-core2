package jandcode.web.test.dummy;

import javax.servlet.*;
import java.util.*;

public class ServletConfigDummy implements ServletConfig {

    private ServletContextDummy servletContext = new ServletContextDummy();

    public String getServletName() {
        return "test";
    }

    public ServletContextDummy getServletContext() {
        return servletContext;
    }

    public String getInitParameter(String name) {
        return null;
    }

    public Enumeration<String> getInitParameterNames() {
        return null;
    }

}
