package jandcode.core.web.cli;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.core.cli.*;
import jandcode.core.web.undertow.*;

/**
 * Запуск web-сервера для приложения
 */
public class ServeCliCmd extends BaseAppCliCmd {

    public void exec() throws Exception {

        UndertowRunner r = new UndertowRunner();
        r.setPort(getPort());
        r.setContext(getContext());
        r.setListenerHost(getAddr());

        System.out.println(UtString.delim("web-server", "-", 70));
        System.out.println(r.getUrl());
        System.out.println(UtString.delim("", "-", 70));

        r.start(getApp());
    }

    public void cliConfigure(CliDef b) {
        b.cmdName("serve");
        b.desc("Запуск web-сервера для приложения");
        //
        b.opt("port")
                .names("-p,--port").arg("PORT")
                .desc("Порт")
                .defaultValue(getPort());
        b.opt("context")
                .names("-c,--context").arg("CONTEXT")
                .desc("Контекст")
                .defaultValue(getContext());
        b.opt("addr")
                .names("-a").arg("ADDR")
                .desc("Адрес для http listener")
                .defaultValue(getAddr());
    }

    private int port = 8080;
    private String context = "/jc";
    private String addr = "0.0.0.0";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}
