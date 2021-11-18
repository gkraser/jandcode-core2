package jandcode.core.web.test;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.web.undertow.*;

import java.net.http.*;

/**
 * Тестовый webserver
 */
public class WebServer {

    private String id;
    private App app;
    private int port;
    private String context;
    private UndertowRunner runner;
    private HttpClient httpClient;

    public WebServer(String id, App app, int port, String context) {
        this.id = id;
        this.app = app;
        this.port = port;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getContext() {
        return context;
    }

    public App getApp() {
        return app;
    }

    //////  server

    private UndertowRunner getRunner() {
        if (runner == null) {
            runner = new UndertowRunner();
            runner.setContext(getContext());
            runner.setPort(getPort());
        }
        return runner;
    }

    public void start() {
        if (getRunner().isStarted()) {
            return;
        }
        try {
            getRunner().start(getApp());
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public void stop() {
        if (!getRunner().isStarted()) {
            return;
        }
        getRunner().stop();
    }

    public String getUrl() {
        return getRunner().getUrl();
    }

    ////// client

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder().build();
        }
        return httpClient;
    }

    public WebClientRequest createRequest() {
        return new WebClientRequest(this);
    }

}
