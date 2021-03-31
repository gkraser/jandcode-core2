package jandcode.core.launcher;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.launcher.impl.*;
import jandcode.core.std.*;

/**
 * Запускалка приложений jandcode-core
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        Launcher r = new Launcher();
        r.run(args);
    }

    //////

    /**
     * Ссылка на приложение.
     * Имеет значение null до момента окончательной загрузки приложения.
     */
    protected App app;

    /**
     * При значении true требуется более подробный вывод сообщений
     */
    protected boolean verbose;

    /**
     * Запуск приложения с командной строки.
     * Метод вызывается из метода main.
     * Сам показывает ошибки.
     *
     * @param argsArr аргументы
     */
    public void run(String[] argsArr) {
        try {
            run1(argsArr);
        } catch (Exception e) {
            showError(e);
            System.exit(1);
        }
    }

    /**
     * Собственно процесс запуска.
     * При ошибке генерит Exception.
     *
     * @param argsArr аргументы
     */
    public void run1(String[] argsArr) throws Exception {
        // первым делом отключаем логирование
        UtLog.logOff();

        // исправляем vfs
        VFS_fix.doFix();

        // аргументы командной строки
        CliArgs args = UtCli.createArgs(argsArr);

        // определяем каталог приложения, не зависимо от app.cfx
        String appDir = UtEnv.resolveAppdir(null);

        // менеджер логов
        AppLogManager logManager = new AppLogManager(appDir);

        // заказана ли помощь
        boolean needHelp = args.containsKey(LauncherConsts.OPT_HELP) || args.containsKey(LauncherConsts.OPT_HELP2);
        args.remove(LauncherConsts.OPT_HELP);
        args.remove(LauncherConsts.OPT_HELP2);

        // заказанная команда
        String cmName = "";
        if (args.getParams().size() > 0) {
            cmName = args.getParams().get(0);
            args.getParams().remove(0);
        }

        String opt;

        // verbose
        opt = LauncherConsts.OPT_VERBOSE;
        if (args.containsKey(opt)) {
            this.verbose = true;
            args.remove(opt);
        }

        // настройка логов
        opt = LauncherConsts.OPT_LOG_CONFIG;
        if (args.containsKey(opt)) {
            String s = args.getString(opt);
            if (!UtString.empty(s)) {
                logManager.setLogConfigFile(s);
            }
            args.remove(opt);
        }

        // включение логов
        opt = LauncherConsts.OPT_LOG;
        if (args.containsKey(opt)) {
            logManager.logOn();
            args.remove(opt);
        }

        // загружаем приложение
        String fileApp;
        opt = LauncherConsts.OPT_APP;
        if (args.containsKey(opt)) {
            String s = args.getString(opt);
            if (!UtFile.isAbsolute(s)) {
                s = UtFile.join(UtFile.getWorkdir(), s);
            }
            if (!UtFile.exists(s)) {
                throw new XError("Файл {0} не найден", s);
            }
            fileApp = s;
            args.remove(opt);
        } else {
            fileApp = UtFile.abs(UtFile.join(appDir, AppConsts.FILE_APP_CONF));
        }

        this.app = AppLoader.load(fileApp, appDir, null, false);

        // настраиваем приложение
        LauncherService svc = this.app.bean(LauncherService.class);
        svc.setVerbose(this.verbose);

        // команда не указана, показываем помощь
        if (UtString.empty(cmName)) {
            help(null);
            return;
        }

        // ищем команду
        Cmd a = svc.findCmd(cmName);
        if (a == null) {
            throw new XError("Команда {0} не найдена", cmName);
        }
        if (needHelp) {
            help(a);
            return;
        }

        // выполняем
        LauncherCmd cmd = a.createInst();
        cmd.setArgs(args);
        cmd.exec();

    }

    //////

    protected void showError(Throwable e) {
        boolean v = this.verbose || this.app == null;
        //
        ErrorFormatter ef = new LauncherErrorFormatter();
        ef.setShowFullStack(v);
        ef.setShowSource(v);
        ef.setShowStack(v);
        //
        System.out.println(ef.getMessage(e));
    }

    ////// help

    protected void buildSysHelp(HelpBuilder h) {
        h.opt(LauncherConsts.OPT_HELP, "Помощь");
        h.opt(LauncherConsts.OPT_LOG, "Включить логирование");
        h.opt(LauncherConsts.OPT_LOG_CONFIG, "Файл с настройками логирования", "FILE", "в порядке приоритета: _logback.xml, logback.xml");
        h.opt(LauncherConsts.OPT_VERBOSE, "Включение режима с большим числом сообщений");
        h.opt(LauncherConsts.OPT_APP, "Имя файла с конфигурацией приложения", "FILE", AppConsts.FILE_APP_CONF);
    }

    protected class LauncherAppInfo extends BaseComp implements AppInfo {

        public String getMainModule() {
            return "jandcode.core.launcher";
        }

        public String getTitle() {
            return "Jandcode Core Launcher";
        }

        public String getCopyright() {
            return "2011-2020 Sergey Kravchenko";
        }

        public String getVersion() {
            return UtVersion.getVersion(getMainModule());
        }

        public Conf getConf() {
            return null;
        }
    }

    protected AppInfo getAppInfo() {
        AppInfo info = app.bean(AppInfo.class);
        if (info.getMainModule().equals("jandcode.core")) {
            info = new LauncherAppInfo();
        }
        return info;
    }

    protected String getHelpTitle() {
        AppInfo appInfo = getAppInfo();

        StringBuilder sb = new StringBuilder();
        sb.append(appInfo.getTitle()).append(" ").append(appInfo.getVersion());

        String s;

        s = appInfo.getCopyright();
        if (!UtString.empty(s)) {
            sb.append(" (C) ").append(s);
        }

        return sb.toString();
    }

    /**
     * Помощь по приложению или по команде (если указана)
     */
    protected void help(Cmd cm) {

        //
        StringBuilder sb = new StringBuilder();
        HelpPrinter hp = new HelpPrinter();

        //
        sb.append(getHelpTitle());

        if (cm != null) {
            sb.append("\n\n").append("Команда: ").append(cm.getName());
            sb.append("\n\n").append(cm.getHelp().getDesc());
        }

        //
        sb.append("\n\n").append("Общие опции:").append("\n");
        HelpBuilderImpl b = new HelpBuilderImpl();
        buildSysHelp(b);
        sb.append(hp.options(b));

        if (cm == null) {
            LauncherService svc = this.app.bean(LauncherService.class);
            NamedList<Cmd> cmds = svc.getCmds();
            if (cmds.size() == 0) {
                sb.append("\n\n").append("Нет доступных команд").append("\n");
            } else {
                sb.append("\n\n").append("Команды:").append("\n");
                sb.append(hp.cmds(cmds));
            }
        } else {
            if (cm.getHelp().getOpts().size() != 0) {
                sb.append("\n\n").append("Опции команды:").append("\n");
                sb.append(hp.options(cm.getHelp()));
            }
        }

        System.out.println(sb);
    }

}
