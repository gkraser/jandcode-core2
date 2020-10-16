package jandcode.core.launcher;

import jandcode.commons.*;
import jandcode.commons.cli.*;

import java.util.*;

/**
 * Печать помощи
 */
public class HelpPrinter {

    public CliHelpFormatter createFormatter() {
        CliHelpFormatter f = UtCli.createHelpFormatter();
        return f;
    }

    /**
     * Получение помощи по опции
     */
    public String getOptHelp(Opt opt) {
        String help = opt.getDesc();
        if (UtString.empty(help)) {
            help = LauncherConsts.NO_HELP;
        }
        if (!UtString.empty(opt.getDefaultArgValue())) {
            help = help + " (по умолчанию: " + opt.getDefaultArgValue() + ")";
        }
        return help;
    }

    /**
     * Получение помощи по опции
     */
    public String getCmdHelp(Cmd cmd) {
        String help = cmd.getHelp().getDesc();
        if (UtString.empty(help)) {
            help = LauncherConsts.NO_HELP;
        }
        return help;
    }

    /**
     * Строка с помощью по опциям
     */
    public String options(Help h) {
        CliHelpFormatter f = createFormatter();
        for (Opt opt : h.getOpts()) {
            f.addOpt(opt.getName(), getOptHelp(opt), opt.getArgName());
        }
        return f.build();
    }

    /**
     * Строка с помощью по командам
     */
    public String cmds(Collection<Cmd> cmds) {
        CliHelpFormatter f = createFormatter();
        for (Cmd cm : cmds) {
            f.addCmd(cm.getName(), UtString.getLine(getCmdHelp(cm), 0));
        }
        return f.build();
    }


}
