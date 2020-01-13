package jandcode.core.web.impl;

import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.core.web.render.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * wrapper для {@link Request}
 */
public class RequestWrapper implements Request {

    protected Request request;

    public RequestWrapper(Request request) {
        this.request = request;
    }

    /**
     * Ссылка на оригинальный request
     */
    public Request getRequest() {
        return request;
    }

    public OutputStream getOutStream() {
        return request.getOutStream();
    }

    public void redirect(String url, Map params) {
        request.redirect(url, params);
    }

    public long getStartTime() {
        return request.getStartTime();
    }

    public String getPathInfo() {
        return request.getPathInfo();
    }

    public void redirect(String url) {
        request.redirect(url);
    }

    public void setAction(IAction action) {
        request.setAction(action);
    }

    public void setContentType(String type) {
        request.setContentType(type);
    }

    public void render(Object data) {
        request.render(data);
    }

    public String ref(String url) {
        return request.ref(url);
    }

    public void checkModified(XDateTime curDate) {
        request.checkModified(curDate);
    }

    public void checkETag(String etag) {
        request.checkETag(etag);
    }

    public void setRender(IRender render) {
        request.setRender(render);
    }

    public IVariantMap getParams() {
        return request.getParams();
    }

    public void setVrtualRoot(String vrtualRoot) {
        request.setVrtualRoot(vrtualRoot);
    }

    public void setHeader(String name, String value) {
        request.setHeader(name, value);
    }

    public HttpServletRequest getHttpRequest() {
        return request.getHttpRequest();
    }

    public IRender getRender() {
        return request.getRender();
    }

    public Writer getOutWriter() {
        return request.getOutWriter();
    }

    public HttpServlet getHttpServlet() {
        return request.getHttpServlet();
    }

    public IVariantMap getSession() {
        return request.getSession();
    }

    public void redirect(String url, boolean virtualRoot, Map params) {
        request.redirect(url, virtualRoot, params);
    }

    public void setException(Throwable exception) {
        request.setException(exception);
    }

    public HttpServletResponse getHttpResponse() {
        return request.getHttpResponse();
    }

    public boolean isOutGet() {
        return request.isOutGet();
    }

    public String getVrtualRoot() {
        return request.getVrtualRoot();
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public String ref(String url, Map params) {
        return request.ref(url, params);
    }

    public Throwable getException() {
        return request.getException();
    }

    public Object getRenderData() {
        return request.getRenderData();
    }

    public App getApp() {
        return request.getApp();
    }

    public IAction getAction() {
        return request.getAction();
    }

    public boolean isContentTypeAssigned() {
        return request.isContentTypeAssigned();
    }

    public String ref(String url, boolean virtualRoot, Map params) {
        return request.ref(url, virtualRoot, params);
    }

    public void setPathInfo(String pathInfo) {
        request.setPathInfo(pathInfo);
    }

    public boolean isAjax() {
        return request.isAjax();
    }

    public IVariantMap getAttrs() {
        return request.getAttrs();
    }

    public String nextId(String prefix) {
        return request.nextId(prefix);
    }

    public void disableCache() {
        request.disableCache();
    }

    public Part getPart(String name) {
        return request.getPart(name);
    }

    public Collection<Part> getParts() {
        return request.getParts();
    }

}
