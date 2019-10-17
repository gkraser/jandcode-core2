package jandcode.core.web.test;

import jandcode.commons.*;
import jandcode.commons.test.*;
import jandcode.core.test.*;
import jandcode.core.web.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.impl.*;
import jandcode.core.web.test.dummy.*;

import java.util.*;

/**
 * Расширение для web-тестов
 */
public class WebTestSvc extends BaseTestSvc {

    public String serverUrl = "http://unittest";
    public String contextPath = "/test";

    protected UtilsTestSvc utils;

    public void setUp() throws Exception {
        super.setUp();
        this.utils = testSvc(UtilsTestSvc.class);
        HttpServletDummy servlet = new HttpServletDummy();
        servlet.setContextPath(contextPath);
        getWebService().setHttpServlet(servlet);
        createRequest("/");
    }

    /**
     * Приложение
     */
    public AppTestSvc getApp() {
        return testSvc(AppTestSvc.class);
    }

    /**
     * WebService
     */
    public WebService getWebService() {
        return getApp().bean(WebService.class);
    }

    /**
     * Последний созданный запрос
     */
    public Request getRequest() {
        return getWebService().getRequest();
    }

    /**
     * Создать запрос с указанными свойствами и сделать его текущим.
     *
     * @param uri    часть строки запроса без host и contextPath
     * @param params параметры запроса
     */
    public Request createRequest(String uri, Map params) {
        //
        HttpServletResponseDummy httpResponse = new HttpServletResponseDummy();
        httpResponse.createOutWriter();

        //
        HttpServletRequestDummy httpRequest = new HttpServletRequestDummy();
        httpRequest.setContextPath(contextPath);
        uri = UtVDir.normalize(uri);
        httpRequest.setPathInfo("/" + uri);
        httpRequest.setRequestURL(serverUrl + contextPath + "/" + uri);
        httpRequest.setRequestURI(contextPath + "/" + uri);

        // params
        if (params != null) {
            for (Object key : params.keySet()) {
                Object value = params.get(key);
                String ks = UtString.toString(key);
                httpRequest.putParameter(ks, UtString.toString(value));
            }
        }

        //
        Request request = new RequestImpl(getApp(), httpRequest, httpResponse);
        ((WebServiceImpl) getWebService()).setRequest(request);
        //
        return request;
    }

    /**
     * Создать запрос с указанными свойствами и сделать его текущим
     */
    public Request createRequest(String url) {
        return createRequest(url, null);
    }

    //////

    /**
     * Обработать запрос
     */
    public void handleRequest(Request request) throws Throwable {
        ((WebServiceImpl) getWebService()).handleRequest(request);
    }

    /**
     * Ответ на запрос в виде текста с обрезанными пробелами
     */
    public String getOutText(Request request) {
        HttpServletResponseDummy r = (HttpServletResponseDummy) request.getHttpResponse();
        return r.getOutText().trim();
    }

    /**
     * Выполнить запрос и вернуть результат как строку с обрезанными пробелами
     */
    public String execRequest(String uri, Map params) throws Throwable {
        Request r = createRequest(uri, params);
        handleRequest(r);
        return getOutText(r);
    }

    /**
     * Выполнить запрос и вернуть результат как строку с обрезанными пробелами
     */
    public String execRequest(String uri) throws Throwable {
        return execRequest(uri, null);
    }

    /**
     * Отрендерить указанный gsp и возвратить полученный текст
     *
     * @param gspName имя gsp
     * @param args    аргументы. Может быть null
     */
    public String renderGsp(String gspName, Map args) throws Exception {
        GspContext ctx = getWebService().createGspContext();
        ITextBuffer buf = ctx.render(gspName, args);
        return buf.toString();
    }

    /**
     * Отрендерить указанный gsp и возвратить полученный текст
     *
     * @param gspName имя gsp
     */
    public String renderGsp(String gspName) throws Exception {
        return renderGsp(gspName, null);
    }

}
