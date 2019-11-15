package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.stopwatch.*;
import jandcode.commons.variant.*;
import jandcode.jc.*;
import jandcode.jc.impl.utils.*;

import java.text.*;
import java.util.*;

/**
 * Базовый класс для запускалок jc
 */
public abstract class BaseMain {

    protected Ansifer ansi = UtAnsifer.getAnsifer();
    protected String appdir;
    protected String workdir;
    protected String projectPath;
    protected CliMap cli;
    protected boolean errorShowFullStack;
    protected Stopwatch stopwatch;
    protected boolean needHeader;
    protected boolean verbose;
    protected JcConfig cfg;
    protected String cmName;
    protected Ctx ctx;

    /**
     * Запуск утилиты с командной строки для вызова в коде или тестах.
     *
     * @param args    аргументы
     * @param appdir  каталог приложения. Этот проект будет загружен первым.
     *                Если не указан, то основной проект не загружается
     * @param workdir где запускать приложение. Этот проект будет основным. Если не указан,
     *                то берется текущий каталог.
     * @return true, если не было ошибок
     */
    public boolean run(String[] args, String appdir, String workdir, boolean throwError) {
        try {
            // отключаем логирование для начала
            UtLog.logOff();

            // исправляем vfs
            VFS_fix.doFix();

            //
            this.appdir = appdir;
            if (UtString.empty(this.appdir)) {
                this.appdir = System.getProperty(JcConsts.PROP_APP_DIR);
            }
            if (!UtString.empty(this.appdir)) {
                this.appdir = UtFile.unnormPath(this.appdir);
            }
            this.workdir = workdir;
            if (UtString.empty(this.workdir)) {
                this.workdir = UtFile.getWorkdir();
            }
            this.cli = new CliMap(args);
            this.stopwatch = new DefaultStopwatch();

            // 
            this.cfg = JcConfigFactory.create();
            if (!UtString.empty(this.appdir)) {
                cfg.setAppdir(this.appdir);
            }

            //
            doRun();

            //
            stopwatch.stop();
            if (needHeader) {
                prn(delim(""));
            } else {
                prn("");
            }
            prn(ansi.color("app-footer", stopwatch.toString()));
            //
            return true;
        } catch (Exception e) {
            String msg = new ErrorFormatterJc(true, verbose,
                    errorShowFullStack).getMessage(UtError.createErrorInfo(e));
            prn(msg);
            if (throwError) {
                throw new XErrorWrap(e);
            }
            return false;
        } finally {
            ansi.ansiOff();
        }
    }

    /**
     * Реализация запуска
     */
    protected abstract void doRun() throws Exception;


    //////

    /**
     * Поиск команды по частичному совпадению
     */
    protected Cm findCmPartial(Project project, String cmName) {
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

    /**
     * Получение команды из проекта по полному по частичному совпадению.
     *
     * @return ошибка, если команда не найдена
     */
    protected Cm getCm(Project project, String cmName) {
        Cm cm = findCmPartial(project, cmName);
        if (cm == null) {
            cm = project.getCm().get(cmName);
        }
        return cm;
    }

    ////// opt

    protected void grabOpt_verbose() {
        String opt = JcConsts.OPT_VERBOSE;
        if (cli.containsKey(opt)) {
            verbose = true;
            cli.remove(opt);
        }
    }

    protected void grabOpt_noAnsi() {
        String opt = JcConsts.OPT_NOANSI;
        if (cli.containsKey(opt)) {
            cli.remove(opt);
        } else {
            ansi.ansiOn();
            new AnsiStyleDef().styleForJc(ansi);
        }
    }

    protected void grabOpt_log() {
        String opt = JcConsts.OPT_LOG;
        if (cli.containsKey(opt)) {
            String s = cli.getString(opt);
            UtLog.logOn(s);
            errorShowFullStack = true;
            cli.remove(opt);
        }
    }

    protected void grabOpt_csc() {
        String opt = JcConsts.OPT_CSC;
        if (cli.containsKey(opt)) {
            String cacheDir = ctx.getTempdirRoot();
            ctx.getLog().info(MessageFormat.format("clear script cache [{0}]", cacheDir));
            UtFile.cleanDir(cacheDir);
            cli.remove(opt);
        }
    }

    protected void grabOpt_cmName() {
        if (cli.getParams().size() > 0) {
            cmName = cli.getParams().get(0);
            cli.getParams().remove(0);
        }

        if (UtString.empty(cmName) ||
                cli.containsKey(JcConsts.OPT_HELP) ||
                cli.containsKey(JcConsts.OPT_HELP2)) {
            needHeader = true;
        }
    }

    /**
     * Сбор остальныйх глобальных опций. ctx уже создан.
     */
    protected void grabOpt_other() {
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

    protected String header(boolean showHome, boolean showProjectPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(ansi.color("app-header", copyright())).append("\n\n");
        if (showHome) {
            if (!UtString.empty(appdir)) {
                appendProp(sb, "jc-home", appdir);
            }
        }
        if (showProjectPath) {
            appendProp(sb, "project", projectPath);
        }
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
        commonOptBuild(z);
        return z.toString();
    }

    protected void commonOptBuild(CliHelp z) {
        z.addOption(JcConsts.OPT_LOG, "Включение логирования. ARG - имя файла в формате logback.\n" +
                "Можно не указывать, тогда используются настройки по умолчанию", true);
        z.addOption(JcConsts.OPT_VERBOSE, "Включение режима с большим числом сообщений");
        z.addOption(JcConsts.OPT_HELP, "Помощь по команде");
        z.addOption(JcConsts.OPT_NOANSI, "Отключить разукрашенный вывод");
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
