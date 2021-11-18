package jandcode.core.web.test;

import jandcode.commons.*;

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

    public HttpRequest createHttpRequest() {
        HttpRequest.Builder b = HttpRequest.newBuilder();

        b.uri(URI.create(getUrl()));

        return b.build();
    }

    /**
     * Выполнить запрос
     */
    public WebClientResponse exec() throws Exception {
        HttpRequest httpRequest = createHttpRequest();
        HttpResponse<?> httpResponse = this.webServer.getHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return new WebClientResponse(this.webServer, httpResponse);
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
