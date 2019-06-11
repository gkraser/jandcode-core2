package jandcode.commons.cli;

import jandcode.commons.*;

import java.util.*;

/**
 * Формирование помощи по опциям.
 * Поддерживает подстветку опций.
 */
public class CliHelp {

    public static final String STYLE_OPT_NAME = "opt-name";
    public static final String STYLE_OPT_HELP = "opt-help";

    private HashMap<String, Opt> items = new HashMap<>();
    private boolean helpToNextLine;
    private boolean enableAnsi = false;
    private String optNameStyle;
    private String optHelpStyle;

    class Opt {
        String name;
        boolean hasArg;
        boolean isParam;
        String help;

        protected String getFormattedName() {
            if (isParam) {
                return name;
            }
            if (!hasArg) {
                return "-" + name;
            } else {
                return "-" + name + ":ARG"; //NON-NLS
            }
        }

    }

    /**
     * Создать экземпляр без поддержки ansi
     */
    public CliHelp() {
    }

    /**
     * Создать экземпляр с или без поддержки ansi
     *
     * @param enableAnsi true - разрешить ansi
     */
    public CliHelp(boolean enableAnsi) {
        this(null, null);
    }

    /**
     * Создать экземпляр с поддержкой ansi
     *
     * @param optNameStyle имя стиля Ansifer для опции.
     *                     Если null - то используется 'opt-name'.
     * @param optHelpStyle имя стиля Ansifer для помощи.
     *                     Если null - то используется 'opt-help'.
     */
    public CliHelp(String optNameStyle, String optHelpStyle) {
        this.enableAnsi = true;
        this.optNameStyle = UtString.empty(optNameStyle) ? STYLE_OPT_NAME : optNameStyle;
        this.optHelpStyle = UtString.empty(optHelpStyle) ? STYLE_OPT_HELP : optHelpStyle;
    }

    /**
     * true - помощь по команде печатать на следующей строке
     */
    public void setHelpToNextLine(boolean helpToNextLine) {
        this.helpToNextLine = helpToNextLine;
    }

    /**
     * Добавить опцию
     *
     * @param name    имя
     * @param help    помощь по опции
     * @param hasArg  имеет ли аргумент
     * @param isParam признак не опции, а параметра (без '-')
     */
    protected void addItem(String name, String help, boolean hasArg, boolean isParam) {
        Opt z = new Opt();
        z.name = name;
        z.isParam = isParam;
        z.hasArg = hasArg;
        z.help = UtString.toString(help);
        items.put(z.name, z);
    }

    /**
     * Добавить опцию
     *
     * @param name   имя
     * @param help   помощь
     * @param hasArg имеет ли аргумент
     */
    public void addOption(String name, String help, boolean hasArg) {
        addItem(name, help, hasArg, false);
    }

    /**
     * Добавить опцию без аргумента
     *
     * @param name имя
     * @param help помощь
     */
    public void addOption(String name, String help) {
        addItem(name, help, false, false);
    }

    /**
     * Добавить параметр
     *
     * @param name имя
     * @param help помощь
     */
    public void addParam(String name, String help) {
        addItem(name, help, false, true);
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
        if (enableAnsi) {
            return UtAnsifer.color(optNameStyle, text);
        } else {
            return text;
        }
    }

    /**
     * Подготовка help опции для вывода
     */
    protected String prepareOptionHelp(String text) {
        if (enableAnsi) {
            return UtAnsifer.color(optHelpStyle, text);
        } else {
            return text;
        }
    }

    /**
     * Отформатированная помощь по опциям
     */
    public String toString() {
        List<Opt> lst = new ArrayList<Opt>();
        lst.addAll(items.values());
        Collections.sort(lst, new Comparator<Opt>() {
            public int compare(Opt o1, Opt o2) {
                return o1.getFormattedName().compareTo(o2.getFormattedName());
            }
        });
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
