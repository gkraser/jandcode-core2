package jandcode.commons;


import jandcode.commons.cli.*;
import jandcode.commons.cli.impl.*;
import jandcode.commons.reflect.*;

import java.util.*;

/**
 * Утилиты для cli
 */
public class UtCli {

    /**
     * Создание экземпляра с параметрами командной строки.
     * <p>
     * Правила разбора:
     * <ul>
     * <li>опция начинается с '-'
     * <li>после первого ':' - параметр опции. Если он не указан - то считается равным ''
     * <li>если опция не начинается с '-', то она считается безымяным параметром и помещается в params
     * <li>если есть несколько одинаковых опций, то всегда рассматривается последняя
     * <li>имена опций являются регистрозависимыми.
     * </ul>
     */
    public static CliArgs createArgs(String[] args) {
        return new CliArgsParser().parse(args);
    }

    /**
     * Создание экземпляра форматтера помощи.
     */
    public static CliHelpFormatter createHelpFormatter() {
        return new CliHelpFormatterImpl();
    }

    /**
     * Создать экземпляр для описания командной строки.
     */
    public static CliDef createCliDef() {
        return new CliDefImpl();
    }

    /**
     * Создать экземпляр для описания командной строки.
     *
     * @param cliConfigure конфигуратор, будет вызван для создаваемого объекта.
     *                     Может быть null, тогда игнорируется.
     */
    public static CliDef createCliDef(CliConfigure cliConfigure) {
        CliDef res = createCliDef();
        if (cliConfigure != null) {
            cliConfigure.cliConfigure(res);
        }
        return res;
    }

    /**
     * Создать экземпляр парзера командной строки для аргументов.
     *
     * @param args аргументы
     */
    public static CliParser createCliParser(String[] args) {
        return new CliParserImpl(args);
    }

    /**
     * Создать экземпляр парзера командной строки для аргументов.
     *
     * @param args аргументы
     */
    public static CliParser createCliParser(List<String> args) {
        return new CliParserImpl(args);
    }

    /**
     * Присвоить значения свойствам объекта inst по значениям полученным из командной
     * строки props.
     *
     * @param inst  куда присваиваем
     * @param props что присваиваем
     */
    public static void bindProps(Object inst, Map<String, Object> props) {
        if (props != null && !props.isEmpty()) {
            ReflectClazz cz = UtReflect.getUtils().getClazz(inst.getClass());
            for (Object key : props.keySet()) {
                String an = key.toString();
                if (an.startsWith("_")) {
                    continue;
                }
                Object av = props.get(key);
                cz.invokeSetter(inst, an, av);
            }
        }
    }
}
