package jandcode.core.web.webxml;

import jandcode.commons.*;
import jandcode.commons.simxml.*;

public class WebXmlUtils {

    /**
     * Записать WebXml в xml версии 3.1
     */
    public SimXml saveToXml(WebXml w) {
        SimXml x = new SimXmlNode("web-app");

        SimXml x1, x2, x3;

        x.getAttrs().put("xmlns", "http://xmlns.jcp.org/xml/ns/javaee");
        x.getAttrs().put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        x.getAttrs().put("xsi:schemaLocation", "http://xmlns.jcp.org/xml/ns/javaee " +
                "http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd");
        x.getAttrs().put("version", "3.1");
        x.getAttrs().put("metadata-complete", "true");

        for (WebXml.Servlet sv : w.getServlets()) {
            x1 = x.addChild("servlet");

            x2 = x1.addChild("servlet-name");
            x2.setText(sv.getServletName());

            x2 = x1.addChild("servlet-class");
            x2.setText(sv.getServletClass());

            for (WebXml.InitParam p : sv.getInitParams()) {
                x2 = x1.addChild("init-param");

                x3 = x2.addChild("param-name");
                x3.setText(p.getParamName());

                x3 = x2.addChild("param-value");
                x3.setText(p.getParamValue());
            }

            x2 = x1.addChild("load-on-startup");
            x2.setText("" + sv.getLoadOnStartup());

        }

        for (WebXml.ServletMapping m : w.getServletMappings()) {
            x1 = x.addChild("servlet-mapping");

            x2 = x1.addChild("servlet-name");
            x2.setText(m.getServletName());

            x2 = x1.addChild("url-pattern");
            x2.setText(m.getUrlPattern());
        }

        for (WebXml.ErrorPage pg : w.getErrorPages()) {
            x1 = x.addChild("error-page");

            if (pg.getErrorCode() != 0) {
                x2 = x1.addChild("error-code");
                x2.setText("" + pg.getErrorCode());
            }

            if (!UtString.empty(pg.getExceptionType())) {
                x2 = x1.addChild("exception-type");
                x2.setText(pg.getExceptionType());
            }

            if (!UtString.empty(pg.getLocation())) {
                x2 = x1.addChild("location");
                x2.setText(pg.getLocation());
            }
        }

        return x;
    }

}
