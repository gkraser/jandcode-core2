package jandcode.db;

import jandcode.commons.conf.*;
import jandcode.commons.variant.*;
import jandcode.core.*;

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
     * Установить значение свойства.
     *
     * @param name  имя свойства
     * @param value значение. Можно использовать подстановки '${propname}',
     *              где propname - имя другого свойства.
     */
    void setProp(String name, Object value);

    /**
     * Установить значения свойств.
     *
     * @param props свойства
     */
    void setProps(Map<String, Object> props);

    /**
     * Свойства базы данных. Имена свойств регистрозависимые.
     * В значениях раскрыты подстановки ${propname}.
     * <p>
     * Для свойств с префиксом 'conn.': префикс убирается и полученное свойство
     * используется как свойство jdbc-соединения (зависит от драйвера jdbc).
     * <p>
     * Только для чтения!
     */
    IVariantMap getProps();

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
     * <p>
     * Только для чтения!
     */
    IVariantMap getProps(String prefix, boolean override);

    //////

    /**
     * Клонировать этот объект
     */
    DbSource cloneComp();

    //////

    /**
     * Сервис для установки соединенй.
     */
    DbConnectionService getConnectionService();

    /**
     * Сервис для установки соединенй без использования пула.
     */
    DbConnectionService getConnectionDirectService();

}
