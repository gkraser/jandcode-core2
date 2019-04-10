package jandcode.web.impl;

import jandcode.commons.datetime.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.action.*;
import jandcode.web.render.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * dummy request.
 * Для тех, кто работает вне запроса, но так или иначе использует ссылку на запрос.
 */
public class RequestDummyImpl extends RequestImpl {

    class VariantMapRO extends VariantMap {
        public Object put(String key, Object value) {
            return null;
        }

        public Object remove(Object key) {
            return null;
        }
    }

    class UnsupportedError extends XError {
        public UnsupportedError() {
            super("Метод не поддерживается вне потока выполнения http-запроса");
        }
    }

    public RequestDummyImpl(App app, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(app, httpRequest, httpResponse);
    }

    protected void init(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        this.startTime = 0;
        this.httpRequest = null;
        this.httpResponse = null;
        pathInfo = "";
        params = new VariantMapRO();
        session = new VariantMapRO();
        renderReady = true;
    }

    //////

    public HttpServletRequest getHttpRequest() {
        throw new UnsupportedError();
    }

    public HttpServletResponse getHttpResponse() {
        throw new UnsupportedError();
    }

    public Writer getOutWriter() {
        throw new UnsupportedError();
    }

    public OutputStream getOutStream() {
        throw new UnsupportedError();
    }

    public String getHeader(String name) {
        return null;
    }

    //////

    public void setPathInfo(String pathInfo) {
    }

    public void render(Object responseData) {
    }

    public void setContentType(String type) {
    }

    public void setVrtualRoot(String vrtualRoot) {
    }

    public void checkModified(XDateTime curDate) {
    }

    public void setAction(IAction action) {
    }

    public void setRender(IRender render) {
    }

    public void setException(Throwable exception) {
    }

    public void setHeader(String name, String value) {
    }

    //////

    public void redirect(String url, boolean virtualRoot, Map params) {
    }

}
