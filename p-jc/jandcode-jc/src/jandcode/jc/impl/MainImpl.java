package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import jandcode.jc.impl.log.*;
import jandcode.jc.std.*;

public class MainImpl extends BaseMain {

    protected void doRun() throws Exception {
        String opt;

        ///////////// предварительная настройка ////////////

        // настройка разукрашки
        grabOpt_noAnsi();

        // настройка консоли
        UtConsole.setupConsoleCharset();

        // verbose
        grabOpt_verbose();

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
        grabOpt_cmName();

        ///////////// запуск ////////////

        // заголовок
        if (needHeader) {
            prn(header(true, true));
        }

        // конфигурируем logback, которые будет принимать все консольные логи
        LogConfigurator.configure(verbose);

        // логирование
        grabOpt_log();

        // включаем секундомер
        stopwatch.start();

        // создаем и настраиваем контекст
        ctx = CtxFactory.createCtx();
        ctx.getLog().setVerbose(verbose);

        // clear script cache
        grabOpt_csc();

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

        // сбор остальных опций
        grabOpt_other();

        // загружаем рабочий проект
        Project project;
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
        Cm cm = getCm(project, cmName);

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

    protected void commonOptBuild(CliHelp z) {
        super.commonOptBuild(z);
        z.addOption(JcConsts.OPT_PROJECTFILE, "Имя файла проекта. По умолчанию " + JcConsts.PROJECT_FILE + " в текущем каталоге",
                true);
        z.addOption(JcConsts.OPT_CSC, "Очистить кеш скриптов");
        z.addOption(JcConsts.OPT_ENV_PROD, "Включить режим production (ctx.env.prod=true)");
    }

}
