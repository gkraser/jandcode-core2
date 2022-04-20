package jandcode.core.dao;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

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
     * Выполнить dao-метод.
     *
     * @param ctxIniter инициализатор контекста dao перед выполнением.
     *                  Может быть null, если не нужен
     * @param name      какой метод
     * @param args      аргументы
     * @return {@link DaoContext}, который использовался для выполнения dao
     */
    DaoContext invokeDao(DaoContextIniter ctxIniter, String name, Object... args) throws Exception;

    /**
     * Список всех зарегистрированных dao.
     * Только для чтения.
     */
    NamedList<DaoHolderItem> getItems();

    /**
     * Список всех зарегистрированных правил для выявления имени daoInvoker.
     * Только для чтения.
     */
    Collection<DaoHolderRule> getRules();

    /**
     * Получить имя daoInvoker для элемента хранилища.
     *
     * @param item элемент
     * @return имя daoInvoker
     */
    String resolveDaoInvokerName(DaoHolderItem item);

    /**
     * Какой {@link DaoInvoker} используется для выполнения по умолчанию.
     * По умолчанию: 'default'
     */
    String getDaoInvokerName();

    /**
     * Добавить правило для получения имени daoInvoker по имени элемента хранилища.
     * Чем позже добавлено - тем выше приоритет правила.
     *
     * @param mask       маска
     * @param daoInvoker имя daoInvoker
     */
    void addRule(String mask, String daoInvoker);

    /**
     * Добавить dao.
     *
     * @param name           имя dao
     * @param className      имя dao-класса
     * @param methodName     имя dao-метода
     * @param daoInvokerName имя DaoInvoker, может быть пустым для автоопределения
     */
    void addItem(String name, String className, String methodName, String daoInvokerName);

    /**
     * Добавить dao.
     *
     * @param name           имя dao
     * @param cls            dao-класс
     * @param methodName     имя dao-метода
     * @param daoInvokerName имя DaoInvoker, можут быть пустим для автоопределения
     */
    void addItem(String name, Class cls, String methodName, String daoInvokerName);

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
     *     dao будет зарегистрировано с именем класса без пакета (из имени будет удален
     *     суффикс '_dao', '_Dao' или 'Dao', первый символ имени будет строчный).
     *     Например для класса 'my.pak.MySuperDao' будет использоваться имя 'mySuper'</li>
     *     <li>recursive: true (по умолчанию) - package будет сканировать рекурсивно</li>
     *     <li>flat: true - вложенные package не будут использоваться
     *     как дополнительные префиксы, используется только имя класса. При значении
     *     false (по умолчанию) к имени класса будет добавлено имя вложенного package</li>
     *     <li>item: дочерние элементы такой же структуры. К их именам будут добавлен
     *     префикс через '/' в виде имени текущего элемента</li>
     *     <li>prefix: если указан атрибут, то используется он в качестве префикса,
     *     вместо имени. Можно указать пустое значение</li>
     *     <li>provider: если указан, то эта конфигурация передается соотвествующему
     *     провайдеру, который предоставит список элементов</li>
     * </ul>
     *
     * @param conf   конфигурация
     * @param prefix префикс имени. Если нужен слеш, нужно явно его указать
     */
    void addItem(Conf conf, String prefix);

}
