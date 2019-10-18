package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.stopwatch.*;
import jandcode.jc.*;
import jandcode.jc.impl.log.*;
import jandcode.jc.impl.utils.*;
import jandcode.jc.std.*;

import java.text.*;

public class MainImpl extends BaseMain {

    private boolean verbose;
    private boolean errorShowFullStack;
    private String workdir;
    private Ctx ctx;
    private Project project;
    private Stopwatch stopwatch;
    private String cmName;
    boolean needHeader;


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
            ansi.ansiOff();
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
            ansi.ansiOn();
            new AnsiStyleDef().styleForJc(ansi, cfg);
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


}
