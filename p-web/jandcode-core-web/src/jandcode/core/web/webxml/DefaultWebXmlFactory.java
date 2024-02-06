package jandcode.core.web.webxml;

import jandcode.core.web.*;

/**
 * Фабрика web.xml по умолчанию для приложений web.
 */
public class DefaultWebXmlFactory implements WebXmlFactory {

    public WebXml createWebXml() {
        WebXml x = new WebXml();
        WebXmlBuilder b = new WebXmlBuilder(x);

        b.addContextParam( "app", "app.cfx");
        b.addContextParam( "log", "true");
        //
        WebXml.Servlet sv = b.addServlet(WebConsts.WEB_SERVLET_NAME, "jandcode.core.web.AppServlet", "/*", 1);

        //
        b.setMultipartConfig(sv);

        //
        b.addErrorPage("/showerror");

        return x;
    }

}
