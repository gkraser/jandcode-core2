package jandcode.commons.cli.impl;

import jandcode.commons.*;
import jandcode.commons.cli.*;

import java.util.*;

/**
 * Формирование помощи по опциям.
 * Поддерживает подстветку опций.
 */
public class CliHelpFormatterImpl implements CliHelpFormatter {

    private Map<String, Opt> items = new HashMap<>();
    private boolean helpToNextLine;
    private boolean ansi = false;
    private String optNameStyle = STYLE_OPT_NAME;
    private String optHelpStyle = STYLE_OPT_HELP;

    class Opt {
        String name;
        String argName;
        boolean isCmd;
        String help;

        Opt(String name, String help, String argName, boolean isCmd) {
            this.name = name;
            this.argName = argName;
            this.isCmd = isCmd;
            this.help = help == null ? "" : help;
        }

        String getFormattedName() {
            if (isCmd) {
                return name;
            }
            if (UtString.empty(argName)) {
                return "-" + name;
            } else {
                return "-" + name + ":" + argName;
            }
        }

    }

    public void setHelpToNextLine(boolean helpToNextLine) {
        this.helpToNextLine = helpToNextLine;
    }

    public void setAnsi(boolean ansi) {
        this.ansi = ansi;
    }

    public void setAnsiStyle(String optNameStyle, String optHelpStyle) {
        if (optNameStyle != null) {
            this.optNameStyle = optNameStyle;
        }
        if (optHelpStyle != null) {
            this.optHelpStyle = optHelpStyle;
        }
    }

    /**
     * Добавить опцию
     */
    protected void addOpt(String name, String help, String argName, boolean isCmd) {
        Opt z = new Opt(name, help, argName, isCmd);
        items.put(z.name, z);
    }

    public void addOpt(String name, String help, String argName) {
        addOpt(name, help, argName, false);
    }

    public void addOpt(String name, String help, boolean hasArg) {
        addOpt(name, help, hasArg ? "ARG" : null, false);
    }

    public void addOpt(String name, String help) {
        addOpt(name, help, null, false);
    }

    public void addCmd(String name, String help) {
        addOpt(name, help, null, true);
    }

    //////

    /**
     * Добавляет в sb форматированное описание опции
     *
     * @param sb куда
     * @param mx длина, до которой нужно расширить пробелами
     */
    protected void appendOpt(StringBuilder sb, Opt opt, int mx) {
        String of = opt.getFormattedName();
        //
        String hlp = opt.help;
        //
        String ar[] = hlp.split("\n");
        String delim = "  ";
        if (helpToNextLine) {
            delim = "        ";
        }
        for (int i = 0; i < ar.length; i++) {
            String s1 = ar[i];
            if (i == 0) {
                sb.append(prepareOptionName(String.format("  %-" + mx + "s", of))); //NON-NLS
                if (helpToNextLine) {
                    sb.append('\n');
                }
            } else {
                if (!helpToNextLine) {
                    sb.append(String.format("  %-" + mx + "s", "")); //NON-NLS
                }
            }
            sb.append(delim).append(prepareOptionHelp(s1)).append("\n");
        }
    }

    /**
     * Подготовка имени опции для вывода
     *
     * @param text текст опции, например: "-f:ARG"
     * @return преобразованный текст
     */
    protected String prepareOptionName(String text) {
        if (ansi) {
            return UtAnsifer.color(optNameStyle, text);
        } else {
            return text;
        }
    }

    /**
     * Подготовка help опции для вывода
     */
    protected String prepareOptionHelp(String text) {
        if (ansi) {
            return UtAnsifer.color(optHelpStyle, text);
        } else {
            return text;
        }
    }

    /**
     * Отформатированная помощь по опциям
     */
    public String build() {
        List<Opt> lst = new ArrayList<>(items.values());
        Collections.sort(lst, Comparator.comparing(Opt::getFormattedName));
        StringBuilder sb = new StringBuilder();
        //
        int mx = 0;
        ///
        for (Opt option : lst) {
            String of = option.getFormattedName();
            mx = Math.max(mx, of.length());
        }
        //
        for (Opt option : lst) {
            appendOpt(sb, option, mx);
        }
        ///
        return UtString.trimLast(sb.toString());
    }

}
