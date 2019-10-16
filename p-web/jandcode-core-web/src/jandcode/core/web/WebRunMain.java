package jandcode.core.web;

import jandcode.commons.*;
import jandcode.commons.cli.*;

/**
 * Запускалка приложения на встроенном web-сервере.
 * Параметры командной строки:
 * -p:PORT
 * -c:CONTEXT
 */
public class WebRunMain {

    public static void main(String[] args) throws Exception {
        WebRunMain m = new WebRunMain();
        m.webRun(args);
    }

    //////

    private String defaultRunner = "jandcode.core.web.undertow.UndertowRunner";
    private String context = "/jc";
    private int port = 8080;

    public void webRun(String[] args) throws Exception {
        UtLog.logOff();

        CliMap cli = new CliMap(args);
        if (cli.containsKey("h")) {
            help();
            return;
        }
        if (cli.containsKey("p")) {
            setPort(cli.getInt("p", getPort()));
        }
        if (cli.containsKey("c")) {
            setContext(cli.getString("c", getContext()));
        }

        IWebRunner wr = (IWebRunner) UtClass.createInst(defaultRunner);
        wr.setPort(getPort());
        wr.setContext(getContext());

        System.out.println(UtString.delim("web run", "-", 80));
        System.out.println(getUrl());
        System.out.println(UtString.delim("", "-", 80));

        wr.start();
    }

    private void help() {
        CliHelp h = new CliHelp();
        h.addOption("p", "Номер порта (по умолчанию: " + getPort() + ")", true);
        h.addOption("c", "Контекст сервлета (по умолчанию: " + getContext() + ")", true);

        //
        System.out.println("Jandcode Web Runner");
        System.out.println("Использование: java " + getClass().getName() + " [-p:PORT] [-c:CONTEXT]");
        System.out.println("Опции:");
        System.out.println(h.toString());
    }


    //////

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
        if (UtString.empty(this.context)) {
            this.context = "/";
        } else if (!this.context.startsWith("/")) {
            this.context = "/" + this.context;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * url, на котором доступен сервер
     */
    public String getUrl() {
        return "http://localhost:" + getPort() + getContext();
    }

}
