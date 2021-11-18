package jandcode.core.web.test;

import jandcode.commons.*;

import java.net.*;
import java.net.http.*;

public class WebClientResponse {

    private WebServer webServer;
    private HttpResponse<?> httpResponse;

    public WebClientResponse(WebServer webServer, HttpResponse<?> httpResponse) {
        this.webServer = webServer;
        this.httpResponse = httpResponse;
    }

    public WebServer getWebServer() {
        return webServer;
    }

    public HttpResponse<?> getHttpResponse() {
        return httpResponse;
    }

    //////

    public int getStatusCode() {
        return getHttpResponse().statusCode();
    }

    public HttpRequest getHttpRequest() {
        return getHttpResponse().request();
    }

    public HttpHeaders getHeaders() {
        return getHttpResponse().headers();
    }

    public URI getUri() {
        return getHttpResponse().uri();
    }

    public String getBodyText() {
        return UtCnv.toString(getHttpResponse().body());
    }

}



