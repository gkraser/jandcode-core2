package jandcode.core.dao;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Хранилище зарегистрированных dao
 */
public interface DaoHolder extends Comp {

    /**
     * Выполнить dao-метод.
     *
     * @param name какой метод
     * @param args аргументы
     * @return то, что метод возвратит
     */
    Object invokeDao(String name, Object... args) throws Exception;

    /**
     * Список всех зарегистрированных dao.
     * Только для чтения.
     */
    NamedList<DaoHolderItem> getItems();

    /**
     * Какой {@link DaoInvoker} используется для выполнения.
     */
    String getDaoInvokerName();

    /**
     * Добавить dao.
     *
     * @param name           имя dao
     * @param className      имя dao-класса
     * @param methodName     имя dao-метода
     * @param daoInvokerName имя dao-менеджера. Можно передать null, тогда будет
     *                       использован тот, который установлен по умолчанию для
     *                       {@link DaoHolder}.
     */
    DaoHolderItem addItem(String name, String className, String methodName, String daoInvokerName);

    /**
     * Добавить dao.
     *
     * @param name           имя dao
     * @param cls            dao-класс
     * @param methodName     имя dao-метода
     * @param daoInvokerName имя dao-менеджера. Можно передать null, тогда будет
     *                       использован тот, который установлен по умолчанию для
     *                       {@link DaoHolder}.
     */
    DaoHolderItem addItem(String name, Class cls, String methodName, String daoInvokerName);

    /**
     * Добавить элемент из конфигурации.
     * Атрибуты conf:
     * <ul>
     *     <li>name: имя dao. В случае указания класса и метода - это и есть имя dao.
     *     В случае указания только класса или пакета - это префикс имени, к которому
     *     будет добавлен слэш '/'</li>
     *     <li>class: имя класса dao</li>
     *     <li>method: имя метода в классе dao. Если не укзано - все методы из класса</li>
     *     <li>package: имя пакета, откуда будут браться dao-классы.
     *     dao будет зарегистрировано с именем класса без пакета, из имени будет удален
     *     суффикс 'Dao' или '_Dao'. Первый символ имени будет строчный.
     *     Например для класса 'my.pak.MySuperDao' будет использоваться имя 'mySuper'</li>
     *     <li>recursive: true (по умолчанию) - package будетсканировать рекурсивно</li>
     *     <li>dao-invoker: имя dao-исполнителя для этого dao. Обычно не указывается,
     *     тогда будет использован тот,что зарегистрирован по умолчанию для holder</li>
     *     <li>item: дочерние элементы такой же структуры.</li>
     * </ul>
     *
     * @param conf   конфигурация
     * @param prefix префикс имени. Если нужен слеш, нужно явно его указать
     */
    void addItem(Conf conf, String prefix);

}
