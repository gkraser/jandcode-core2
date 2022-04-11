package jandcode.core.dbm.sql;

import jandcode.core.dbm.*;

import java.util.*;

/**
 * Построитель sql
 */
public interface SqlBuilder extends IModelLink {

    /**
     * Делает список полей из объекта. fields может быть:
     * <ul>
     * <li>Domain (или IDomainLink) - берутся все не вычисляемые поля</li>
     * <li>Map - берутся все ключи</li>
     * <li>List - берутся все строковые представления элементов. Если элемент INamed,
     * то берется его имя</li>
     * <li>String - строка разделяется через ','</li>
     * <li>Store (или StoreRecord, или IStoreFieldHolder) - берутся все не вычисляемые
     * и не системные поля</li>
     * </ul>
     * <p>
     * Дубли исключаются.
     * Системные поля исключаются. Под системными подразумеваются поля с '-' в имени.
     *
     * @param fields источник имен полей
     * @return список полей
     */
    List<String> makeFieldList(Object fields);

    /**
     * Делает список полей из объекта, исключая указанные поля.
     * см: {@link SqlBuilder#makeFieldList(java.lang.Object)}.
     *
     * @param fields        источник имен полей
     * @param excludeFields источник исключаемых имен полей
     * @return список полей
     */
    List<String> makeFieldList(Object fields, Object excludeFields);

    /**
     * Делает простой sql insert
     *
     * @param tableName для какой таблицы
     * @param fields    какие поля
     * @return sql text
     */
    String makeSqlInsert(String tableName, Object fields);

    /**
     * Делает простой sql update
     *
     * @param tableName   для какой таблицы
     * @param fields      какие поля
     * @param whereFields какие поля использовать в условии where.
     *                    Обязательно должно быть хотя бы одно.
     * @return sql text
     */
    String makeSqlUpdate(String tableName, Object fields, Object whereFields);

    /**
     * Делает простой sql update
     *
     * @param tableName   для какой таблицы
     * @param whereFields какие поля использовать в условии where.
     *                    Обязательно должно быть хотя бы одно.
     * @return sql text
     */
    String makeSqlDelete(String tableName, Object whereFields);

}
