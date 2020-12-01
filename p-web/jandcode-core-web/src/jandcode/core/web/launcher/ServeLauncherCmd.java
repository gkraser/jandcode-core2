package jandcode.core.web.launcher;

import jandcode.commons.*;
import jandcode.core.launcher.*;
import jandcode.core.web.undertow.*;

/**
 * Запуск web-сервера
 */
public class ServeLauncherCmd extends BaseLauncherCmd {

    public void help(HelpBuilder h) throws Exception {
        h.desc("Запуск web-сервера для приложения");
        //
        h.opt("p", "Порт", "PORT", D_PORT);
        h.opt("c", "Контекст", "CONTEXT", D_CONTEXT);
        h.opt("a", "Адрес для http listener", "IP", D_ADDR);
    }

    public void exec() throws Exception {

        setPort(getArgs().getInt("p"));
        setContext(getArgs().getString("c"));
        setAddr(getArgs().getString("a"));

        UndertowRunner r = new UndertowRunner();
        r.setPort(getPort());
        r.setContext(getContext());
        r.setListenerHost(getAddr());

        System.out.println(UtString.delim("web-server", "-", 70));
        System.out.println(r.getUrl());
        System.out.println(UtString.delim("", "-", 70));

        r.start(getApp());
    }

    //////

    private static int D_PORT = 8080;
    private static String D_CONTEXT = "/jc";
    private static String D_ADDR = "0.0.0.0";

    private int port;
    private String context;
    private String addr;

    public int getPort() {
        if (this.port <= 0) {
            return D_PORT;
        }
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContext() {
        if (UtString.empty(this.context)) {
            this.context = D_CONTEXT;
        }
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAddr() {
        if (UtString.empty(this.addr)) {
            return D_ADDR;
        }
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}

