package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.stopwatch.*;
import jandcode.commons.variant.*;
import jandcode.jc.*;
import jandcode.jc.impl.log.*;
import jandcode.jc.impl.utils.*;
import jandcode.jc.std.*;

import java.text.*;
import java.util.*;

public class MainImpl {

    // показывать ли декорации
    public boolean showDecoration = true;

    private boolean verbose;
    private boolean errorShowFullStack;
    private String appdir;
    private String workdir;
    private CliMap cli;
    private Ctx ctx;
    private String projectPath;
    private Project project;
    private Stopwatch stopwatch;
    private String cmName;
    boolean needHeader;

    private Ansifer ansi = UtAnsifer.getAnsifer();
    private boolean ansiInstallHere = false;

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

            //
            this.appdir = appdir;
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
            if (ansiInstallHere) {
                ansi.uninstall();
            }
        }
    }

    //////

    private void doRun() throws Exception {
        String s, opt;

        ///////////// предварительная настройка ////////////

        VFS_fix.doFix();

        // загружаем конфигурацию
        JcConfig cfg = JcConfigFactory.load(workdir);
        if (!UtString.empty(appdir)) {
            cfg.setAppdir(appdir);
        }

        // настройка разукрашки
        opt = JcConsts.OPT_NOANSI;
        if (cli.containsKey(opt)) {
            cli.remove(opt);
        } else {
            if (!ansi.isInstalled()) {
                ansiInstallHere = true;
                ansi.install();
                new AnsiStyleDef().styleForJc(ansi, cfg);
            }
        }

        // настройка консоли
        UtConsole.setupConsoleCharset();

        // verbose
        opt = JcConsts.OPT_VERBOSE;
        if (cli.containsKey(opt)) {
            verbose = true;
            cli.remove(opt);
        }

        // переопределение файла проекта
        boolean projectPathAssigned = false;
        projectPath = workdir;
        opt = JcConsts.OPT_PROJECTFILE;
        if (cli.containsKey(opt)) {
            projectPath = cli.getString(opt);
            if (UtString.empty(projectPath)) {
                throw new XError(UtLang.t("Не указано имя файла в опции -f"));
            }
            projectPathAssigned = true;
            cli.remove(opt);
        }

        // имя команды
        if (cli.getParams().size() > 0) {
            cmName = cli.getParams().get(0);
            cli.getParams().remove(0);
        }

        if (UtString.empty(cmName) || cli.containsKey(JcConsts.OPT_HELP)) {
            needHeader = true;
        }

        ///////////// запуск ////////////

        // заголовок
        if (needHeader) {
            prn(header());
        }

        // конфигурируем logback, которые будет принимать все консольные логи
        LogConfigurator.configure(verbose);

        // логирование
        opt = JcConsts.OPT_LOG;
        if (cli.containsKey(opt)) {
            s = cli.getString(opt);
            UtLog.logOn(s);
            errorShowFullStack = true;
            cli.remove(opt);
        }

        // включаем секундомер
        stopwatch.start();

        // создаем и настраиваем контекст
        ctx = CtxFactory.createCtx();
        ctx.getLog().setVerbose(verbose);

        // clear script cache
        opt = JcConsts.OPT_CSC;
        if (cli.containsKey(opt)) {
            String cacheDir = ctx.getTempdirRoot();
            ctx.getLog().info(MessageFormat.format("clear script cache [{0}]", cacheDir));
            UtFile.cleanDir(cacheDir);
        }

        // env-prod
        opt = JcConsts.OPT_ENV_PROD;
        if (cli.containsKey(opt)) {
            ctx.getEnv().setProd(true);
        } else {
            // false ставим явно, что бы можно было отлавливать изменения флага в дальнейшем
            ctx.getEnv().setProd(false);
        }

        // инициализируем и там же загружаем корневой проект, если есть
        ctx.applyConfig(cfg);

        // загружаем рабочий проект
        String projectPathResolved = ctx.resolveProjectFile(workdir, projectPath);
        if (projectPathResolved != null) {
            project = ctx.load(projectPathResolved);
        } else {
            if (projectPathAssigned) {
                throw new XError(UtLang.t("Не найден проект: {0}", projectPath));
            }
            // даем полное имя файла
            projectPath = UtFile.join(projectPath, JcConsts.PROJECT_FILE);
            // нет файла проекта, запустили в пустом каталоге
            // создаем проект-заглушку
            project = new ProjectImpl(ctx, projectPath);
        }
        if (project.getCm().getItems().size() == 0) {
            // команд нет
            // делаем из него спец-проект по созданию проектов
            project.include(NoProject.class);
        }

        // проект загружен, исполняем

        if (UtString.empty(cmName)) {
            // нет команды, выводим общий help
            if (needHeader) {
                prn(headerDelim());
                prn(help(project));
            }
            return;
        }

        // выбираем команду
        Cm cm = findCm(project, cmName);
        if (cm == null) {
            cm = project.getCm().get(cmName);
        }

        if (needHeader) {
            prn(headerDelim());
        }

        // помощь?
        if (cli.containsKey(JcConsts.OPT_HELP) || cli.containsKey(JcConsts.OPT_HELP2)) {
            prn(helpCm(project, cm));
            return;
        }

        if (ctx.getEnv().isProd()) {
            ctx.warn("set env.prod=true");
        }

        // выполняем
        cm.exec(cli);
        //

        // все
    }

    //////

    /**
     * Поиск команды по частичному совпадению
     */
    private Cm findCm(Project project, String cmName) {
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

    private void prn(String s) {
        System.out.println(s);
    }

    private String version() {
        VersionInfo vi = new VersionInfo("jandcode.jc");
        return vi.getVersion();
    }

    private String delim(String msg) {
        return ansi.color("app-delim", UtString.delim(msg, "~", JcConsts.DELIM_LEN + 5));
    }

    private void appendProp(StringBuilder sb, String n, String v) {
        sb.append(" * ").append(ansi.color("app-info-name", n)).append(": ").append(ansi.color("app-info-value", v)).append("\n");
    }

    private String header() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(ansi.color("app-header", copyright())).append("\n\n");
        if (!UtString.empty(appdir)) {
            appendProp(sb, "jc-home", appdir);
        }
        appendProp(sb, "project", projectPath);
        return UtString.trimLast(sb.toString());
    }

    private String headerCm(Cm cm) {
        StringBuilder sb = new StringBuilder();
        appendProp(sb, "command", cm.getName());
        return UtString.trimLast(sb.toString());
    }

    private String headerDelim() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(delim(""));
        return sb.toString();
    }

    private String copyright() {
        return "Jandcode Jc " + version() + " (C) 2011-2019 Sergey Kravchenko";
    }

    private String commonOpt() {
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
