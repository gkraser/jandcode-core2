package jandcode.commons.cli;

import jandcode.commons.*;

import java.util.*;

/**
 * Утилиты для формирования помощи для командной строки.
 */
public class CliHelpUtils {

    private static final String NO_HELP = "(no help)"; // NLS

    public CliHelpFormatter createFormatter() {
        return UtCli.createHelpFormatter();
    }

    /**
     * Описание для команд и опций без описания
     */
    public String getNoHelp() {
        return NO_HELP;
    }

    /**
     * Описание опции.
     */
    public String getOptDesc(ICliOpt opt) {
        String help = opt.getDesc();
        if (UtString.empty(help)) {
            help = getNoHelp();
        }
        if (opt.hasDefaultValue()) {
            String defValue = opt.getDefaultValue();
            if (!UtString.empty(defValue)) {
                help = help + " (по умолчанию: " + defValue + ")";
            }
        }
        return help;
    }

    /**
     * Имя опции
     */
    public String getOptName(ICliOpt opt) {
        String nm = UtString.join(opt.getNames(), ", ");
        if (opt.hasArg()) {
            nm += " " + opt.getArg();
        }
        return nm;
    }

    /**
     * Строка с помощью по опциям
     */
    public String getOptHelp(Collection<? extends ICliOpt> opts) {
        CliHelpFormatter f = createFormatter();
        for (var opt : opts) {
            f.addCmd(getOptName(opt), getOptDesc(opt));
        }
        return f.build();
    }

    /**
     * Строка с помощью по команде. Одна строка.
     */
    public String getCmdHelp(CliCmd cmd) {
        CliDef cliDef = UtCli.createCliDef(cmd);

        String s = UtString.getLine(cliDef.getDesc(), 0);
        if (UtString.empty(s)) {
            s = getNoHelp();
        }

        return s;
    }

    /**
     * Строка с помощью по коимандам
     */
    public String getCmdHelp(Map<String, CliCmd> cmds) {
        CliHelpFormatter f = createFormatter();
        for (var en : cmds.entrySet()) {
            String cmdName = en.getKey();
            String cmdHelp = getCmdHelp(en.getValue());
            f.addCmd(cmdName, cmdHelp);
        }
        return f.build();
    }

    /**
     * Строка usage: опции и парамеры
     */
    public String getUsage(Collection<? extends ICliOpt> opts) {
        String positional = "";
        String options = "";
        boolean hasCommonOptions = false;
        for (var opt : opts) {
            if (opt.isPositional()) {
                String s = opt.getArg();
                if (opt.isMulti()) {
                    s = s + "*";
                }
                if (!opt.isRequired()) {
                    s = "[" + s + "]";
                }
                positional = positional + " " + s;
            } else {
                if (opt.isRequired()) {
                    String s = UtString.join(opt.getNames(), "|");
                    if (opt.hasArg()) {
                        s = s + " " + opt.getArg();
                    }
                    options = options + " " + s;
                } else {
                    hasCommonOptions = true;
                }
            }
        }
        String s = "";
        if (hasCommonOptions) {
            s = "[OPTIONS]";
        }
        s = s + options + positional;
        return s;
    }

}
