package jandcode.core.web.impl;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.error.*;
import jandcode.commons.idgen.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.core.web.render.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * Реализация Request по умолчанию
 */
public class RequestImpl extends BaseComp implements Request {

    protected HttpServletRequest httpRequest;
    protected HttpServletResponse httpResponse;
    protected String pathInfo;
    protected IVariantMap params;
    protected IVariantMap session;
    protected Object renderData;
    protected Writer outWriter;
    protected OutputStream outStream;
    protected String vrtualRoot;
    protected long startTime;
    protected String contentType;
    protected IAction action;
    protected Throwable exception;
    protected IRender render;
    protected Map<String, String> headersCache;
    protected IVariantMap attrs;
    protected IdGenerator idGenerator;
    protected boolean disableCacheExecuted;
    protected boolean renderReady;
    protected RequestContext context;

    public RequestImpl(App app, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        setApp(app);
        init(httpRequest, httpResponse);
    }

    protected void init(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // время начала запроса
        this.startTime = System.currentTimeMillis();

        //
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;

        // pathinfo
        pathInfo = getHttpRequest().getPathInfo();
        pathInfo = UtVDir.normalize(pathInfo); // '/' превращается в пусто!

        // attrs
        attrs = new VariantMap();

        // params
        params = new VariantMap();
        HttpServletRequest hreq = getHttpRequest();
        Enumeration en = hreq.getParameterNames();
        while (en.hasMoreElements()) {
            String pn = en.nextElement().toString();
            params.put(pn, hreq.getParameter(pn));
        }

        // session
        session = new VariantMapWrap(new HttpSessionMap(getHttpRequest().getSession()));

    }

    //////

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public HttpServlet getHttpServlet() {
        return getApp().bean(WebService.class).getHttpServlet();
    }

    //////

    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public IVariantMap getParams() {
        return params;
    }

    public IVariantMap getSession() {
        return session;
    }

    ////

    /**
     * Устанавливается в процессе обработки запроса в true в момент,
     * когда можно пользоваться outWriter или outStream
     */
    public void setRenderReady(boolean renderReady) {
        this.renderReady = renderReady;
    }

    public Writer getOutWriter() {
        if (!renderReady) {
            throw new XError("getOutWriter можно использовать только в IRender#render");
        }
        if (outStream != null) {
            throw new XError("getOutWriter after getOutStream");
        }
        if (outWriter == null) {
            getHttpResponse().setCharacterEncoding(UtString.UTF8);
            try {
                if (contentType == null) {
                    setContentType("text/html");
                }
                getHttpResponse().setContentType(contentType);
                if (headersCache != null) {
                    // устанавливаем закешированные заголовки
                    for (String hname : headersCache.keySet()) {
                        String hvalue = headersCache.get(hname);
                        getHttpResponse().setHeader(hname, hvalue);
                    }
                }
                outWriter = getHttpResponse().getWriter();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return outWriter;
    }

    public OutputStream getOutStream() {
        if (!renderReady) {
            throw new XError("getOutStream можно использовать только в IRender#render");
        }
        if (outWriter != null) {
            throw new XError("getOutStream after getOutWriter");
        }
        if (outStream == null) {
            try {
                if (contentType == null) {
                    setContentType("application/octet-stream");
                }
                getHttpResponse().setContentType(contentType);
                if (headersCache != null) {
                    // устанавливаем закешированные заголовки
                    for (String hname : headersCache.keySet()) {
                        String hvalue = headersCache.get(hname);
                        getHttpResponse().setHeader(hname, hvalue);
                    }
                }
                outStream = getHttpResponse().getOutputStream();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return outStream;
    }

    public boolean isOutGet() {
        return outStream != null || outWriter != null;
    }

    public Object getRenderData() {
        return renderData;
    }

    public void render(Object responseData) {
        this.renderData = responseData;
    }

    public void setContentType(String type) {
        if (UtString.empty(type)) {
            contentType = null;
        } else {
            contentType = type;
        }
        if (contentType != null && isOutGet()) {
            getHttpResponse().setContentType(contentType);
        }
    }

    public boolean isContentTypeAssigned() {
        return contentType != null;
    }

    //////

    public String getVrtualRoot() {
        return vrtualRoot;
    }

    public void setVrtualRoot(String vrtualRoot) {
        this.vrtualRoot = vrtualRoot;
    }

    ////// ref

    public String ref(String url, boolean virtualRoot, Map params) {
        UrlBuilder u = new UrlBuilder();
        if (!(url == null || url.length() == 0 || url.startsWith("?")) &&
                !UtWeb.isAbsoluteUrl(url)) {
            u.setRoot(true);
            if (httpRequest != null) {
                u.append(httpRequest.getContextPath());
                u.append(httpRequest.getServletPath());
            } else {
                u.append(getHttpServlet().getServletContext().getContextPath());
            }
            if (virtualRoot) {
                u.append(getVrtualRoot());
            }
        }
        u.append(url);
        u.append(params);

        return u.toString();
    }

    public String ref(String url, Map params) {
        return ref(url, true, params);
    }

    public String ref(String url) {
        return ref(url, true, null);
    }

    ////// redirect

    public void redirect(String url, boolean virtualRoot, Map params) {
        throw new HttpRedirect(ref(url, virtualRoot, params));
    }

    public void redirect(String url, Map params) {
        redirect(url, true, params);
    }

    public void redirect(String url) {
        redirect(url, true, null);
    }

    ////// utils

    public void checkModified(XDateTime curDate) {
        // дата модификации в запросе
        XDateTime lastModReq = null;
        String lastModReqText = getHttpRequest().getHeader("If-Modified-Since");
        if (!UtString.empty(lastModReqText)) {
            lastModReq = UtWeb.stringToDateGMT(lastModReqText);
        }

        // проверяем дату модификации, если нужно
        XDateTime lastMod = curDate.clearMSec();
        if (lastModReq != null) {
            if (lastMod.compareTo(lastModReq) <= 0) {
                // не поменялся
                throw new HttpError(304);
            }
        }

        // устанавливаем дату модификации
        getHttpResponse().setHeader("Last-Modified", UtWeb.dateToStringGMT(lastMod));
    }

    public void checkETag(String etag) {
        if (UtString.empty(etag)) {
            return;
        }

        // etag в запросе
        String etagReq = getHttpRequest().getHeader("If-None-Match");

        // проверяем
        if (etagReq != null) {
            if (etagReq.equals(etag)) {
                // не поменялся
                throw new HttpError(304);
            }
        }

        // устанавливаем etag
        getHttpResponse().setHeader("ETag", etag);
    }

    public long getStartTime() {
        return startTime;
    }

    //////

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

    //////

    public IRender getRender() {
        return render;
    }

    public void setRender(IRender render) {
        this.render = render;
    }

    //////

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    //////

    public void setHeader(String name, String value) {
        if (headersCache == null) {
            headersCache = new LinkedHashMap<>();
        }
        headersCache.put(name, value);
        if (isOutGet()) {
            getHttpResponse().setHeader(name, value);
        }
    }

    public String getHeader(String name) {
        return getHttpRequest().getHeader(name);
    }

    public boolean isAjax() {
        String hdr = getHttpRequest().getHeader("X-Requested-With");
        return !UtString.empty(hdr);
    }

    public IVariantMap getAttrs() {
        return attrs;
    }

    public String nextId(String prefix) {
        if (idGenerator == null) {
            idGenerator = new IdGeneratorDefault(null, false);
        }
        return idGenerator.nextId(prefix);
    }

    public void disableCache() {
        if (this.disableCacheExecuted) {
            return;
        }
        this.disableCacheExecuted = true;
        //
        String pastDate = UtWeb.dateToStringGMT(UtDateTime.create("1973-09-15"));
        setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        setHeader("Pragma", "no-cache");
        setHeader("Last-Modified", pastDate);
        setHeader("Expires", pastDate);
    }

    public Part getPart(String name) {
        try {
            return httpRequest.getPart(name);
        } catch (Exception e) {
            throw new XErrorMark(e, "part: " + name);
        }
    }

    public Collection<Part> getParts() {
        try {
            return httpRequest.getParts();
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    //////

    public RequestContext getContext() {
        if (this.context == null) {
            synchronized (this) {
                if (this.context == null) {
                    WebService svc = getApp().bean(WebService.class);
                    if (svc instanceof WebServiceImpl) {
                        RequestContextFactory cf = ((WebServiceImpl) svc).getRequestContextFactory();
                        this.context = cf.createRequestContext(this);
                    } else {
                        throw new XError("{0} не поддерживает создание RequestContextFactory", svc.getClass());
                    }
                }
            }
        }
        return this.context;
    }

}
