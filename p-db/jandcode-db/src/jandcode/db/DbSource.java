package jandcode.db;

import jandcode.core.*;
import jandcode.commons.conf.*;

import java.util.*;

/**
 * Источник для базы данных. Содержит информацию о параметрах базы данных:
 * драйвер, пользователь, параметры соединения, параметры jdbc и т.д.
 * Является фабрикой объектов {@link Db}.
 */
public interface DbSource extends Comp, IConfLink, BeanFactoryOwner {

    /**
     * Возвращает новый экземпляр Db.
     * Соединение автоматически не устанавливается.
     */
    Db createDb();

    /**
     * Возвращает кешированный в рамках текущего потока экземпляр Db.
     * Соединение автоматически не устанавливается.
     */
    Db getDb();

    /**
     * Тип базы данных. Например 'oracle', 'mysql'.
     * Для одного типа базы данных может быть реализованно различные драйвера,
     * однако внешним инструментам нужно знать, что за тип базы используется.
     * Определяется драйвером базы данных.
     */
    String getDbType();

    /**
     * Драйвер
     */
    DbDriver getDbDriver();


    ////// props

    /**
     * Свойства базы данных. Имена свойств регистрозависимые.
     * В значениях можно использовать подстановки ${propname}.
     * <p>
     * Для свойств с префиксом 'conn.': префикс убирается и полученное свойство
     * используется как свойство jdbc-соединения (зависит от драйвера jdbc).
     */
    Map<String, String> getProps();

    /**
     * Возвращает свойства с указанным префиксом.
     * <p>
     * Если override=true то возвращает копию getProps() с перекрытием
     * свойствами 'prefix.XXX' свойств 'XXX'.
     * Например имеем в getProps() [username:'AAA',password:'BBB',system.username:'CCC'].
     * Тогда getProps('system', true) вернет [username:'ССС',password:'BBB']
     * <p>
     * Если override=false то возвращает только свойства с указанным префиксом,
     * причем префикс удаляется.
     * Например имеем в getProps() [username:'AAA',password:'BBB',system.username:'CCC'].
     * Тогда getProps('system', false) вернет [username:'ССС']
     */
    Map<String, String> getProps(String prefix, boolean override);

    //////

    /**
     * Клонировать этот объект
     */
    DbSource cloneComp();

}
