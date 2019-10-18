package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import jandcode.commons.cli.*;
import jandcode.commons.variant.*;
import jandcode.jc.*;

import java.util.*;

/**
 * Базовый класс для запускалок jc
 */
public abstract class BaseMain {

    protected Ansifer ansi = UtAnsifer.getAnsifer();
    protected String appdir;
    protected String projectPath;
    protected CliMap cli;

    //////

    /**
     * Поиск команды по частичному совпадению
     */
    protected Cm findCm(Project project, String cmName) {
        if (cmName.indexOf('-') != -1) {
            return null;
        }
        Cm found = null;
        for (Cm cm : project.getCm().getItems()) {
            String nm = cm.getName();
            String[] ar = nm.split("-");
            for (String s1 : ar) {
                if (s1.startsWith(cmName)) {
                    if (found != null) {
                        return null; //больше одной совпадают
                    }
                    found = cm;
                }
            }
        }
        return found;
    }

    ////// help

    protected void prn(String s) {
        System.out.println(s);
    }

    protected String version() {
        return UtVersion.getVersion("jandcode.jc");
    }

    protected String delim(String msg) {
        return ansi.color("app-delim", UtString.delim(msg, "~", JcConsts.DELIM_LEN + 5));
    }

    protected void appendProp(StringBuilder sb, String n, String v) {
        sb.append(" * ").append(ansi.color("app-info-name", n)).append(": ").append(ansi.color("app-info-value", v)).append("\n");
    }

    protected String header() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(ansi.color("app-header", copyright())).append("\n\n");
        if (!UtString.empty(appdir)) {
            appendProp(sb, "jc-home", appdir);
        }
        appendProp(sb, "project", projectPath);
        return UtString.trimLast(sb.toString());
    }

    protected String headerCm(Cm cm) {
        StringBuilder sb = new StringBuilder();
        appendProp(sb, "command", cm.getName());
        return UtString.trimLast(sb.toString());
    }

    protected String headerDelim() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(delim(""));
        return sb.toString();
    }

    protected String copyright() {
        return "Jandcode Jc " + version() + " (C) 2011-2019 Sergey Kravchenko";
    }

    protected String commonOpt() {
        CliHelp z = new CliHelp("app-opt-name", null);
        z.addOption(JcConsts.OPT_LOG, "Включение логирования. ARG - имя файла в формате logback.\n" +
                "Можно не указывать, тогда используются настройки по умолчанию", true);
        z.addOption(JcConsts.OPT_VERBOSE, "Включение режима с большим числом сообщений");
        z.addOption(JcConsts.OPT_PROJECTFILE, "Имя файла проекта. По умолчанию " + JcConsts.PROJECT_FILE + " в текущем каталоге",
                true);
        z.addOption(JcConsts.OPT_HELP, "Помощь по команде");
        z.addOption(JcConsts.OPT_NOANSI, "Отключить разукрашенный вывод");
        z.addOption(JcConsts.OPT_CSC, "Очистить кеш скриптов");
        z.addOption(JcConsts.OPT_ENV_PROD, "Включить режим production (ctx.env.prod=true)");
        return z.toString();
    }

    public String help(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append("Использование: jc COMMAND [OPTIONS]").append("\n");
        sb.append("\n");
        sb.append("Опции:").append("\n");
        sb.append(commonOpt()).append("\n\n");
        //
        Collection<Cm> res = project.getCm().getItems();
        if (res.size() == 0) {
            sb.append("Нет доступных команд").append("\n");
        } else {
            sb.append("Команды:").append("\n");
            CliHelp h = new CliHelp("app-cm-name", null);
            for (Cm cm : res) {
                String hlp;
                if (cm.getOpts().size() > 0) {
                    hlp = ansi.color("app-opt-name", "-h ");
                } else {
                    hlp = "   ";
                }
                hlp += UtString.getLine(cm.getHelp(), 0);
                h.addParam(cm.getName(), hlp);
            }
            sb.append(h.toString()).append("\n");
        }
        //
        return sb.toString();
    }

    public String helpCm(Project project, Cm cm) {
        StringBuilder sb = new StringBuilder();
        //
        sb.append("Команда: ").append(ansi.color("app-cm-name", cm.getName())).append("\n\n");
        //
        sb.append(cm.getHelp()).append("\n\n");
        //
        sb.append("Общие опции:").append("\n");
        sb.append(commonOpt()).append("\n\n");
        //
        CliHelp z = new CliHelp("opt-name", null);
        for (CmOpt opt : project.getCm().getOpts(cm, cli)) {
            boolean hasArg = VariantDataType.fromObject(opt.getDefaultValue()) != VariantDataType.BOOLEAN;
            z.addOption(opt.getName(), opt.getHelp(), hasArg);
        }
        String s = z.toString();
        if (s.length() > 0) {
            sb.append("Опции команды:").append("\n");
            sb.append(s);
        }
        //
        return sb.toString();
    }

}
