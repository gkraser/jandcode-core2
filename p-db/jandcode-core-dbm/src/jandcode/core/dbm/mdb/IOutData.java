package jandcode.core.dbm.mdb;

import java.util.*;

/**
 * Методы для вывода всяких данных на консоль для целей отладки.
 */
public interface IOutData {

    /**
     * Вывести map на консоль в удобочитаемом виде.
     *
     * @param m         что выводить
     * @param showClass выводить дополнительно имена классов значений
     */
    void outMap(Map m, boolean showClass);

    /**
     * Вывести map на консоль в удобочитаемом виде.
     *
     * @param m что выводить
     */
    void outMap(Map m);

    /**
     * Вывести json представление указанного объекта.
     *
     * @param data что выводить
     */
    void outJson(Object data);

    /**
     * Вывести таблицу на консоль
     *
     * @param data  объект с данными (store, storeRecord, map)
     * @param limit сколько записей выводить, -1 - без лимита
     */
    void outTable(Object data, int limit);

    /**
     * Вывести таблицу на консоль
     *
     * @param data объект с данными (store, storeRecord, map)
     */
    void outTable(Object data);

}
