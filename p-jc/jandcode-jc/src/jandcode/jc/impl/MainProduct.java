package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import jandcode.jc.impl.log.*;

public class MainProduct extends BaseMain {

    /**
     * Запуск утилиты с командной строки. Вызывается из os.
     */
    public static void main(String[] args) throws Exception {
        MainProduct main = new MainProduct();
        if (!main.run(args, null, null, false)) {
            System.exit(1);
        }
    }

    protected void doRun() throws Exception {
        String opt;

        ///////////// предварительная настройка ////////////

        cfg.setRunAsProduct(true);

        // настройка разукрашки
        grabOpt_noAnsi();

        // настройка консоли
        UtConsole.setupConsoleCharset();

        // verbose
        grabOpt_verbose();

        //
        String mainInclude = System.getProperty("jandcode.jc.include");
        if (UtString.empty(mainInclude)) {
            throw new XError("Неопределено свойство include");
        }

        // имя команды
        grabOpt_cmName();

        ///////////// запуск ////////////

        // заголовок
        if (needHeader) {
            prn(header(false, false));
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

        // инициализируем
        ctx.applyConfig(cfg);

        // создаем проект
        Project project = new ProjectImpl(ctx, UtFile.join(cfg.getAppdir(), "dummy.jc"));
        project.include(mainInclude);

        // исполняем
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

        // выполняем
        cm.exec(cli);
        //

        // все
    }


}
