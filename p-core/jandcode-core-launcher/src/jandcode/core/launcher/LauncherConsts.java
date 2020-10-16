package jandcode.core.launcher;

public class LauncherConsts {

    public static final String OPT_HELP = "h";
    public static final String OPT_HELP2 = "?";
    public static final String OPT_LOG = "log";
    public static final String OPT_LOG_CONFIG = "log-config";
    public static final String OPT_VERBOSE = "v";

    /**
     * Cистемные опции, используемые в main. Их нельзя использовать в командах.
     */
    public static final String[] OPT_ALL = {
            OPT_HELP,
            OPT_HELP2,
            OPT_LOG,
            OPT_LOG_CONFIG,
            OPT_VERBOSE,
    };

    /**
     * Для команд и опций без описания
     */
    public static final String NO_HELP = "(no help)"; // NLS


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
