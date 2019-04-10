package jandcode.jc;

/**
 * Константы для jc
 */
public class JcConsts {

    public static final String OPT_HELP = "h";
    public static final String OPT_HELP2 = "?";
    public static final String OPT_NOANSI = "no-ansi";
    public static final String OPT_VERBOSE = "v";
    public static final String OPT_LOG = "log";
    public static final String OPT_PROJECTFILE = "f";
    public static final String OPT_CSC = "csc";
    public static final String OPT_ENV_PROD = "env-prod";

    /**
     * Cистемные опции, используемые в Main. Их нельзя использовать в командах.
     */
    public static final String[] OPT_ALL = {
            OPT_HELP,
            OPT_HELP2,
            OPT_NOANSI,
            OPT_VERBOSE,
            OPT_LOG,
            OPT_PROJECTFILE,
            OPT_CSC,
            OPT_ENV_PROD,
    };

    /**
     * Имя файла проекта по умолчанию.
     */
    public static final String PROJECT_FILE = "project.jc";

    /**
     * Имя файла для маркировка каталога с корневым проектом многомодульного проекта
     */
    public static final String JC_ROOT_FILE = ".jc-root";

    /**
     * Имя файла с конфигурацией
     */
    public static final String CONFIG_FILE = "jc-cfg.cfx";

    /**
     * Каталог со библиотеками внутри проекта по умолчанию
     */
    public static final String LIB_DIR = "lib";

    /**
     * Имя переменной стреды, которая содержит список путей, разделенных ';'
     * или ':'. Эти пути указывают на проекты, которые нужно загрузить перед тем,
     * как начнется загрузка текущего проекта.
     */
    public static final String ENV_JC_PATH = "JC_PATH";

    /**
     * Для команд и опций без описания
     */
    public static final String NO_HELP = "(нет описания)"; // NLS

    /**
     * Имя каталога с данными jc
     */
    public static final String JC_DATA_DIR = "jc-data";

    /**
     * Имя каталога с метаданными данными jc.
     * В этот каталог генерируются некоторые данные.
     * Его можно безопасно удалять. Должен игнорироваться системой контроля версий.
     */
    public static final String JC_METADATA_DIR = "_jc";

    /**
     * Имя логгера logback, через который выводятся информационные сообщения
     */
    public static final String LOGGER_CONSOLE = "jc.console";

    ////// события

    /**
     * Добавлен проект, но еще не загружен.
     */
    public static class Event_ProjectAdded extends BaseJcEvent {
        public Event_ProjectAdded(Project p) {
            setProject(p);
        }
    }

    /**
     * Проект загружен
     */
    public static class Event_ProjectAfterLoad extends BaseJcEvent {
        public Event_ProjectAfterLoad(Project p) {
            setProject(p);
        }
    }

    /**
     * Проект загружен и все события Event_ProjectAfterLoad отработали
     */
    public static class Event_ProjectAfterLoadAll extends BaseJcEvent {
        public Event_ProjectAfterLoadAll(Project p) {
            setProject(p);
        }
    }

    /**
     * Добавлен провайдер библиотек
     */
    public static class Event_LibProviderAdded extends BaseJcEvent {
        private ILibProvider provider;

        public Event_LibProviderAdded(ILibProvider provider) {
            this.provider = provider;
        }

        public ILibProvider getProvider() {
            return provider;
        }
    }


    //////

    /**
     * Системное свойство - каталог установленного jc
     */
    public static final String PROP_APP_DIR = "jandcode.jc.appdir";

    /**
     * Длина печатаемого разделителя
     */
    public static final int DELIM_LEN = 76;

    /**
     * Атрибут в jar-манифесте со списком зависимостей этой библиотеки.
     * Значения - список библиотек через ','
     */
    public static final String MANIFEST_LIB_DEPENDS = "Jandcode-Lib-Depends";

    /**
     * Атрибут в jar-манифесте со именем библиотеки
     */
    public static final String MANIFEST_LIB_NAME = "Jandcode-Lib-Name";

    /**
     * Атрибут в jar-манифесте со именем библиотеки
     */
    public static final String MANIFEST_LIB_VERSION = "Jandcode-Lib-Version";

    /**
     * Атрибут в jar-манифесте со maven groupId
     */
    public static final String MANIFEST_LIB_GROUP_ID = "Jandcode-Lib-GroupId";

    /**
     * Имя каталога в temp для временных файлов jc
     */
    public static final String TEMP_DIR = ".jc-cache";

    /**
     * Версия по умолчанию, которая подставляется в случае неизвестной версии
     * в проектах и библиотеках.
     */
    public static final String VERSION_DEFAULT = "0.1-SNAPSHOT";

    /**
     * Группа зависимостей prod
     */
    public static final String DEPENDS_PROD = "prod";

    /**
     * Группа зависимостей dev
     */
    public static final String DEPENDS_DEV = "dev";

    /**
     * Группа зависимостей exdev
     */
    public static final String DEPENDS_EXDEV = "exdev";

    /**
     * Группа зависимостей all
     */
    public static final String DEPENDS_ALL = "all";

    /**
     * Группы зависимостей, которые используются для раскрытия зависимостей по умолчанию
     */
    public static final String EXPAND_DEPENDS_DEFAULT = "prod,exdev";

    /**
     * true - если опция системная
     */
    public static boolean isOptSys(String opt) {
        for (int i = 0; i < OPT_ALL.length; i++) {
            if (OPT_ALL[i].equals(opt)) {
                return true;
            }
        }
        return false;
    }

}
