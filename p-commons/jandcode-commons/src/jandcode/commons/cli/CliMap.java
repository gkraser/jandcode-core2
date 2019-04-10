package jandcode.commons.cli;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Командная строка в виде Map.
 * <p>
 * Правила разбора:
 * <ul>
 * <li>опция начинается с '-'
 * <li>после первого ':' - параметр опции. Если он не указан - то считается равным ''
 * <li>если опция не начинается с '-', то она считается безымяным параметром и помещается в params
 * <li>если есть несколько одинаковых опций, то всегда рассматривается последняя
 * <li>имена опций являются регистрозависимыми.
 * </ul>
 * Ключи доступны после разбора как элементы Map
 */
public class CliMap extends VariantMap {

    private List<String> params = new ArrayList<String>();
    private String[] args;

    public CliMap() {
    }

    public CliMap(String[] args) {
        parse(args);
    }

    public CliMap(List<String> args) {
        String[] a = args.toArray(new String[0]);
        parse(a);
    }

    /**
     * Оригинальные аргументы
     */
    public String[] getArgs() {
        if (args == null) {
            args = new String[0];
        }
        return args;
    }

    /**
     * Параметры (без опций)
     */
    public List<String> getParams() {
        return params;
    }

    public void clear() {
        super.clear();
        getParams().clear();
        args = null;
    }

    /**
     * Разбор командной строки
     *
     * @param args что разбирать
     */
    public void parse(String[] args) {
        clear();
        this.args = args;
        if (args == null || args.length == 0) {
            return;
        }
        //
        for (String arg : args) {
            if (arg == null || arg.length() == 0) {
                continue;
            }
            if (arg.startsWith("-")) {
                // option
                String opt = arg.substring(1);
                String value = "";
                int a = opt.indexOf(':');
                if (a != -1) {
                    value = opt.substring(a + 1);
                    opt = opt.substring(0, a);
                }
                put(opt, value);
            } else {
                getParams().add(arg);
            }
        }

    }

}
