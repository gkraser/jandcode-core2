package jandcode.commons.cli;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.log.*;

import java.util.*;

/**
 * Точка входа в приложение командной строки.
 * Базовый класс. Дает основной функционал.
 * Экземпляр создается в методе main и управляет тем, что и как будет исполненно.
 */
public class CliLauncher implements CliConfigure {

    /**
     * Имя команды по умолчанию для случаев, когда используется запуск
     * в режиме только одной команды.
     */
    public static final String CMD_DEFAULT = "default";

    private static boolean _initEnvGlobal;

    private List<String> args = new ArrayList<>();
    private String appDir;
    private boolean exitOnError = true;
    private boolean showError = true;
    private boolean logEnabled;
    private String logConfig;
    private boolean verbose;
    private String cmdName;
    private String header;
    private String desc;
    private String footer;
    private String version;
    private Map<String, CliCmd> cmds = new LinkedHashMap<>();
    private List<CliExtension> extensions = new ArrayList<>();

    public class DefaultExtension implements CliExtension {

        public void beforeCmdExec(CliCmd cmd) {
            if (cmd instanceof ICliLauncherLinkSet) {
                ((ICliLauncherLinkSet) cmd).setCliLauncher(CliLauncher.this);
            }
        }
    }

    public CliLauncher(String[] args) {
        initEnv(args);
    }

    protected void initEnv(String[] args) {
        // аргументы командной строки
        if (args != null) {
            this.args.addAll(Arrays.asList(args));
        }

        //
        if (!_initEnvGlobal) {
            _initEnvGlobal = true;
            initEnvGlobal();
        }

        // определяем каталог приложения
        this.appDir = UtEnv.resolveAppdir(null);

        // расширение по умолчанию
        addExtension(new DefaultExtension());
    }

    /**
     * Глобальная инициализация. Вызывается только один раз, т.к. управляет окружением.
     * Можно перекрывать, но вызов super обязателен.
     */
    protected void initEnvGlobal() {
        // первым делом отключаем логирование
        UtLog.logOff();

        // исправляем vfs
        VFS_fix.doFix();
    }

    ////// props

    /**
     * Оригинальные аргументы командной строки
     */
    public List<String> getArgs() {
        return args;
    }

    /**
     * Каталоr приложения. Определяется автоматически.
     */
    public String getAppDir() {
        return appDir;
    }

    public void setAppDir(String appDir) {
        this.appDir = appDir;
    }

    /**
     * При значении true: если возникла ошибка при выполнении, будет вызван System.exit(1)
     */
    public boolean isExitOnError() {
        return exitOnError;
    }

    public void setExitOnError(boolean exitOnError) {
        this.exitOnError = exitOnError;
    }

    /**
     * При значении true: будет показано сообщение об ошибке в консоле.
     */
    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }

    /**
     * Разрешено ли логирование
     */
    public boolean isLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    /**
     * Файл для настройки логирования.
     */
    public String getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(String logConfig) {
        this.logConfig = logConfig;
    }

    /**
     * При значении true требуется более подробный вывод сообщений
     */
    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Версия приложения
     */
    public String getVersion() {
        if (version == null) {
            return UtVersion.getVersion(UtEnv.resolveMainClass());
        }
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void version(String version) {
        this.setVersion(version);
    }

    /**
     * Имя команды. Имя запускаемого файла.
     */
    public String getCmdName() {
        if (cmdName == null) {
            return UtEnv.resolveCmdName();
        }
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public void cmdName(String cmdName) {
        this.setCmdName(cmdName);
    }

    /**
     * Заголовок приложения. Первая строка в помощи по приложению.
     * Обычно содержит имя приложения и версию.
     */
    public String getHeader() {
        if (header == null) {
            return getCmdName() + " " + getVersion();
        }
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void header(String header) {
        this.setHeader(header);
    }

    /**
     * Описание приложения.
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void desc(String desc) {
        this.setDesc(desc);
    }

    /**
     * Подвал описания приложения
     */
    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void footer(String footer) {
        this.setFooter(footer);
    }

    //////

    /**
     * Зарегистрированные команды.
     */
    public Map<String, CliCmd> getCmds() {
        return cmds;
    }

    /**
     * Добавить команду с именем, как установлено в cliConfigure
     */
    public void addCmd(CliCmd cmd) {
        if (cmd == null) {
            throw new XError("cmd is null");
        }
        CliDef cliDef = UtCli.createCliDef(cmd);
        String cmdName = cliDef.getCmdName();
        if (UtString.empty(cmdName)) {
            cmdName = cmd.getClass().getSimpleName();
        }
        getCmds().put(cmdName, cmd);
    }

    /**
     * Добавить команду с указанным именем
     */
    public void addCmd(String cmdName, CliCmd cmd) {
        if (cmd == null) {
            throw new XError("cmd is null");
        }
        getCmds().put(cmdName, cmd);
    }

    /**
     * Список зарегистрированных расширений
     */
    public List<CliExtension> getExtensions() {
        return extensions;
    }

    /**
     * Добавить расширение
     */
    public void addExtension(CliExtension ext) {
        if (ext == null) {
            throw new XError("ext is null");
        }
        if (ext instanceof ICliLauncherLinkSet) {
            ((ICliLauncherLinkSet) ext).setCliLauncher(this);
        }
        this.extensions.add(ext);
    }

    //////

    /**
     * Показ ошибки на консоле
     */
    public void showError(Throwable e) {
        boolean v = isVerbose();
        //
        ErrorFormatter ef = new CliErrorFormatter();
        ef.setShowFullStack(v);
        ef.setShowSource(v);
        ef.setShowStack(v);
        //
        System.out.println(ef.getMessage(e));
    }

    //////

    /**
     * Полный процесс выполнения. Вызывается в методе main.
     * Про ошибке: показывает ошибку и завершает выполнение с ненулевым кодом.
     */
    public void exec() {
        try {
            doExec();
        } catch (Exception e) {
            if (isShowError()) {
                showError(e);
            }
            if (isExitOnError()) {
                System.exit(1);
            } else {
                throw new XErrorWrap(e);
            }
        }
    }

    /**
     * Выполнить указанную команду.
     * При вызове этого метода считается, что приложение имеет только одну команду.
     */
    public void exec(CliCmd cmd) {
        getCmds().clear();
        getCmds().put(CMD_DEFAULT, cmd);
        exec();
    }

    //////

    /**
     * Реализация процесса выполнения. При ошибке генерируется исключение.
     * Вызывать в тестах или в особой среде исполнения.
     */
    public void doExec() throws Exception {
        if (getCmds().size() == 0) {
            throw new XError("No commands defined for {0}", this.getClass().getName());
        }

        // определяем режим исполнения: набор команд или без набора
        boolean multiCommands = getCmds().size() > 1 || getCmds().get(CMD_DEFAULT) == null;

        // аргументы
        CliParser cli = UtCli.createCliParser(this.args);

        // забираем свои опции
        CliDef cliDef = UtCli.createCliDef(this);
        Map<String, Object> opts = cli.extractOpts(cliDef);
        UtCli.bindProps(this, opts);

        // присваиваем опции расширениям
        for (var ext : getExtensions()) {
            UtCli.bindProps(ext, opts);
        }

        // был ли запрос помощи
        boolean needHelp = opts.containsKey("needHelp");

        // настраиваем логирование
        if (isLogEnabled()) {
            AppLogManager lm = UtLog.createAppLogManager(getAppDir());
            if (!UtString.empty(getLogConfig())) {
                lm.setLogConfigFile(getLogConfig());
            }
            lm.logOn();
        }

        // какая команда
        CliCmd cmd = null;
        String cmdName = CMD_DEFAULT;

        if (!multiCommands) {
            cmd = getCmds().get(CMD_DEFAULT);
        } else {
            cmdName = cli.extractParam();
            if (cmdName == null) {
                needHelp = true;
            } else {
                cmd = getCmds().get(cmdName);
                if (cmd == null) {
                    throw new XError("Command [{0}] not found");
                }
            }
        }

        // известна команда
        if (needHelp) {
            showHelp(multiCommands, cmd, cmdName);
            return;
        }

        // опции команды
        CliDef cliDefCmd = UtCli.createCliDef(cmd);
        Map<String, Object> optsCmd = cli.extractOpts(cliDefCmd);
        if (cli.getArgs().size() > 0) {
            throw new XError("Неизвестные аргументы {0}", cli.getArgs().toString());
        }
        UtCli.bindProps(cmd, optsCmd);

        // иньекции
        for (var ext : getExtensions()) {
            ext.beforeCmdExec(cmd);
        }

        cmd.exec();
    }

    ////// help

    public void cliConfigure(CliDef b) {
        b.opt("needHelp")
                .names("-h,--help").desc("Помощь");
        b.opt("logEnabled")
                .names("-log").desc("Включить логирование");
        b.opt("logConfig")
                .names("-log-config").arg("FILE").desc("Настройки логирования");
        b.opt("verbose")
                .names("-v").desc("Включение режима с большим числом сообщений");

        for (var ext : getExtensions()) {
            ext.cliConfigure(b);
        }

        b.cmdName(getCmdName());

        String s;

        s = getDesc();
        if (!UtString.empty(s)) {
            b.desc(s);
        }
        s = getFooter();
        if (!UtString.empty(s)) {
            b.footer(s);
        }
    }

    /**
     * Построить помощь
     *
     * @param multiCommands true - многокомандный режим, когда приложение выполняет одну
     *                      из зарегистрированных команд
     * @param cmd           Экземпляр команды. null, если требуется построить помощь
     *                      для приложения в целом.
     * @param cmdName       имя команды. null, если cmd==null
     * @return строка с помощью для печати на консоле
     */
    protected String buildHelp(boolean multiCommands, CliCmd cmd, String cmdName) {

        CliHelpUtils hm = new CliHelpUtils();

        // описание командной строки приложения
        CliDef appCliDef = UtCli.createCliDef(this);

        // описание командной строки команды
        CliDef cmdCliDef = UtCli.createCliDef(cmd);

        if (!multiCommands) {
            // только одна команда, объединяем опции
            appCliDef.joinFrom(cmdCliDef);
        }

        boolean helpForApp = !multiCommands || cmd == null;
        CliDef mainCliDef = helpForApp ? appCliDef : cmdCliDef;

        StringBuilder sb = new StringBuilder();
        String s;

        // header
        s = getHeader();
        if (!UtString.empty(s)) {
            sb.append(s).append("\n");
        }

        if (!helpForApp) {
            sb.append("\n").append("Команда: ").append(cmdName).append("\n");
        }

        // desc
        s = mainCliDef.getDesc();
        if (!UtString.empty(s)) {
            sb.append("\n").append(s).append("\n");
        }

        // usage
        s = hm.getUsage(mainCliDef.getOpts());
        if (!UtString.empty(s)) {
            sb.append("\n").append("Использование: ");
            sb.append(appCliDef.getCmdName()).append(" ");
            if (!helpForApp) {
                sb.append(cmdName).append(" ");
            } else if (multiCommands) {
                sb.append("COMMAND").append(" ");
            }
            sb.append(s).append("\n");
        }

        // общие опции
        s = "";
        if (appCliDef.getOpts().size() > 0) {
            if (helpForApp) {
                if (multiCommands) {
                    s = "Общие опции";
                } else {
                    s = "Опции";
                }
            } else {
                s = "Общие опции";
            }
            sb.append("\n").append(s).append(":\n");
            sb.append(hm.getOptHelp(appCliDef.getOpts()));
            sb.append("\n");
        }

        // команды
        if (helpForApp && multiCommands) {
            s = "Команды";
            sb.append("\n").append(s).append(":\n");
            sb.append(hm.getCmdHelp(getCmds()));
            sb.append("\n");
        }

        // опции команды
        s = "";
        if (!helpForApp && cmdCliDef.getOpts().size() > 0) {
            s = "Опции команды";
            sb.append("\n").append(s).append(":\n");
            sb.append(hm.getOptHelp(cmdCliDef.getOpts()));
            sb.append("\n");
        }

        // footer
        s = mainCliDef.getFooter();
        if (!UtString.empty(s)) {
            sb.append("\n").append(s).append("\n");
        }

        //
        s = sb.toString().trim();
        return s;
    }

    protected void showHelp(boolean multiCommands, CliCmd cmd, String cmdName) {
        System.out.println(buildHelp(multiCommands, cmd, cmdName));
    }

}
