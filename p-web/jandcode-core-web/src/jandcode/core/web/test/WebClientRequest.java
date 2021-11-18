package jandcode.core.web.test;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.net.*;
import java.net.http.*;
import java.util.*;

public class WebClientRequest {

    private WebServer webServer;
    private String uri = "/";
    private Map<String, String> queryParams = new LinkedHashMap<>();

    public WebClientRequest(WebServer webServer) {
        this.webServer = webServer;
    }

    //////

    public HttpRequest createHttpRequest(IHttpRequestBuilder builder) {
        HttpRequest.Builder b = HttpRequest.newBuilder();

        b.uri(URI.create(getUrl()));

        if (builder != null) {
            builder.build(b);
        }

        return b.build();
    }

    public HttpRequest createHttpRequest() {
        return createHttpRequest(null);
    }

    /**
     * Выполнить запрос
     */
    public WebClientResponse exec(IHttpRequestBuilder builder) {
        HttpRequest httpRequest = createHttpRequest(builder);
        HttpResponse<?> httpResponse = null;
        try {
            httpResponse = this.webServer.getHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        return new WebClientResponse(this.webServer, httpResponse);
    }

    /**
     * Выполнить запрос
     */
    public WebClientResponse exec() throws Exception {
        return exec(null);
    }

    //////

    public String getUri() {
        String s = this.uri;
        if (UtString.empty(s)) {
            s = "/";
        }
        if (!s.startsWith("/")) {
            s = "/" + s;
        }
        return s;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return this.webServer.getUrl() + getUri();
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

}
