package jandcode.commons.conf;

import jandcode.commons.conf.impl.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.util.*;

/**
 * Conf. Средство хранения конфигураций.
 * Структура типа "map of maps".
 * Методы для получения/установки значения параметр name рассматривают как путь.
 * Т.е. можно так:
 * <pre>{@code
 * conf.setValue("a/b/c", 20)
 * conf.getValue("a/b/c")
 * conf.get("a/b/c")
 * conf.put("a/b/c", 20)
 * }</pre>
 * Остальные методы Map оперируют ключами, а не путями.
 * <pre>{@code
 * conf.containsKey("a/b/c")  // так не работает
 * conf.containsKey("a")      // работает так
 * }</pre>
 * <p>
 * Если значением свойства является Conf, то он возвращается
 * с именем, равным имени свойства, из которого он был получен.
 * Имя Conf - регистрозависимое.
 */
public interface Conf extends IVariantMap, INamed {

    /**
     * Создать экземпляр Conf
     */
    static Conf create() {
        return new ConfImpl(null);
    }

    /**
     * Создать поименнованный экземпляр Conf
     */
    static Conf create(String name) {
        return new ConfImpl(name);
    }

    /**
     * Создать экземпляр Conf из Conf, Map, Collection
     */
    static Conf create(Object data) {
        Conf x = new ConfImpl(null);
        x.join(data);
        return x;
    }

    //////

    /**
     * Проверка на совпадение имени.
     */
    boolean hasName(String name);

    /**
     * Есть ли имя.
     */
    boolean hasName();

    /**
     * Значение свойства как поименнованный Conf.
     * Если значение свойства отсутсвует или не Conf, то генерируется ошибка.
     */
    Conf getConf(String path);

    /**
     * Найти по пути.
     *
     * @param path путь, разделитель '/'
     * @return null, если не найден или путь указыват на простое свойство
     */
    Conf findConf(String path);

    /**
     * Найти по пути.
     *
     * @param path             путь, разделитель '/'
     * @param createIfNotExist создать, если нету или простое свойство
     * @return null, если не найден или путь указыват на простое свойство
     */
    Conf findConf(String path, boolean createIfNotExist);

    /**
     * Значения всех свойств типа Conf в виде списка.
     * В списке поименнованные объекты. В качестве имени используется имя свойства.
     * Список всегда не null, даже если нет свойств типа Conf.
     */
    Collection<Conf> getConfs();

    /**
     * Значения всех свойств типа Conf в виде списка у объекта по пути path.
     * В списке поименнованные объекты. В качестве имени используется имя свойства.
     * Список всегда не null, даже если нет свойств типа Conf.
     */
    Collection<Conf> getConfs(String path);

    /**
     * Объеденить с указанным объектом
     *
     * @param data может быть Conf, Map, Collection
     */
    void join(Object data);

    /**
     * Создать копию
     */
    Conf cloneConf();

    /**
     * Откуда взялся этот Conf.
     */
    ConfOrigin origin();

}

