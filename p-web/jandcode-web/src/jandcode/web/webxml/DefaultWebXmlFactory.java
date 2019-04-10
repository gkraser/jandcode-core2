package jandcode.web.webxml;

import jandcode.web.*;

/**
 * Фабрика web.xml по умолчанию для приложений web.
 */
public class DefaultWebXmlFactory implements WebXmlFactory {

    public WebXml createWebXml() {
        WebXml x = new WebXml();
        WebXmlBuilder b = new WebXmlBuilder(x);

        //
        WebXml.Servlet sv = b.addServlet(WebConsts.WEB_SERVLET_NAME, "jandcode.web.AppServlet", "/*", 1);
        b.addInitParam(sv, "app", "app.cfx");
        b.addInitParam(sv, "log", "_logback.xml");

        //
        b.addErrorPage("/showerror");

        return x;
    }

}
