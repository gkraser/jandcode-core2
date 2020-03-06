package jandcode.core.apex.launcher;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.core.web.undertow.*;

/**
 * Запуск apex-приложения.
 */
public class ApexLauncher extends BaseAppLauncher {

    public static final String OPT_LOG = "log";
    public static final String OPT_LOG_CONFIG = "log-config";
    public static final String OPT_HELP = "h";
    public static final String OPT_HELP2 = "?";
    public static final String OPT_VERBOSE = "v";

    protected void onRun() throws Exception {
        CliArgs cli = getCli();

        boolean needHelp = cli.containsKey(OPT_HELP) || cli.containsKey(OPT_HELP2);
        if (needHelp) {
            help();
            return;
        }

        String opt;

        //
        opt = OPT_LOG_CONFIG;
        if (cli.containsKey(opt)) {
            String s = cli.getString(opt);
            if (!UtString.empty(s)) {
                setLogConfigFile(s);
            }
            cli.remove(opt);
        }

        //
        opt = OPT_LOG;
        if (cli.containsKey(opt)) {
            logOn();
            cli.remove(opt);
        }

        //
        opt = OPT_VERBOSE;
        if (cli.containsKey(opt)) {
            setVerbose(true);
            cli.remove(opt);
        }

        //
        runServe();
    }

    public void help() {
        CliHelpFormatter h = UtCli.createHelpFormatter();
        //
        h.addOpt(OPT_HELP, "Помощь");
        h.addOpt(OPT_LOG, "Включить логирование");
        h.addOpt(OPT_VERBOSE, "Включить режим подробных сообщений");
        h.addOpt(OPT_LOG_CONFIG, "Файл с настройками логирования " +
                " (по умолчанию в порядке приоритета: _logback.xml, logback.xml)", "FILE");

        // serve
        h.addOpt("p", "Порт (по умолчанию: 8080)", "PORT");
        h.addOpt("c", "Контекст (по умолчанию: /jc)", "CONTEXT");

        System.out.println(getHelpHeader());
        System.out.println("");
        System.out.println("Запуск web-приложения");
        System.out.println("");
        System.out.println("Опции:");
        System.out.println(h);
    }

    //////

    public void runServe() throws Exception {
        CliArgs cli = getCli();

        UndertowRunner r = new UndertowRunner();
        r.setPort(cli.getInt("p", 8080));
        r.setContext(cli.getString("c", "/jc"));

        System.out.println(UtString.delim("web-server", "-", 70));
        System.out.println(r.getUrl());
        System.out.println(UtString.delim("", "-", 70));

        r.start(getApp());
    }

}
