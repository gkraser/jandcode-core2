package jandcode.core.db;

import jandcode.commons.conf.*;
import jandcode.core.*;

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
     * Возвращает новый экземпляр Db.
     * Соединение автоматически не устанавливается.
     *
     * @param direct при значении true создается экземпляр, настроенный
     *               на direct-соединения (без пула).
     */
    Db createDb(boolean direct);

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
     * В значениях раскрыты подстановки ${propname}.
     * <p>
     * Для свойств с префиксом 'conn.': префикс убирается и полученное свойство
     * используется как свойство jdbc-соединения (зависит от драйвера jdbc).
     */
    DbSourceProps getProps();


    //////

    /**
     * Клонировать этот объект
     */
    DbSource cloneComp();

}
