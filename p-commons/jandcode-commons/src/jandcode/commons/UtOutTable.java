package jandcode.commons;

import jandcode.commons.named.*;
import jandcode.commons.outtable.*;

import java.util.*;

/**
 * Утилиты для вывода информации в виде таблиц.
 * Для отладочных целей.
 */
public class UtOutTable {

    private static OutTableEngine engine = new OutTableEngine();

    public static OutTableEngine getOutTableEngine() {
        return engine;
    }

    /**
     * Создать {@link OutTableSaver} для указанного объекта.
     *
     * @param data объект с данными. Может быть как объектом типа {@link OutTable},
     *             так и объектом, который можно к нему привести
     */
    public static OutTableSaver createOutTableSaver(Object data) {
        return engine.createOutTableSaver(data);
    }

    /**
     * Создать {@link OutTable} для указанного объекта.
     *
     * @param data если тип {@link OutTable}, то возвращается он сам.
     *             иначе производиься попытка привести объект к типу {@link OutTable}.
     */
    public static OutTable createOutTable(Object data) {
        return engine.createOutTable(data);
    }

    /**
     * Вывести таблицу на консоль
     *
     * @param data  объект с данными. Может быть как объектом типа {@link OutTable},
     *              так и объектом, который можно к нему привести
     * @param limit сколько записей выводить, -1 - без лимита
     */
    public static void outTable(Object data, int limit) {
        OutTableSaver tb = createOutTableSaver(data);
        tb.setLimit(limit);
        System.out.println(tb.save().toString());
    }

    /**
     * Вывести таблицу на консоль
     *
     * @param data данные
     */
    public static void outTable(Object data) {
        outTable(data, -1);
    }

    /**
     * Вывести список таблиц на консоль
     *
     * @param lst   список объектов с данными для outTable
     * @param limit сколько записей выводить, -1 - без лимита
     */
    public static void outTableList(Collection lst, int limit) {
        int num = 0;
        for (Object item : lst) {
            String title = "#" + num;
            if (item instanceof INamed nm) {
                title = nm.getName();
            }
            System.out.println("");
            System.out.println(title);
            outTable(item, limit);
        }
    }

    /**
     * Вывести список таблиц на консоль
     *
     * @param lst список объектов с данными для outTable
     */
    public static void outTableList(Collection lst) {
        outTableList(lst, -1);
    }

}
