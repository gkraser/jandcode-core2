package jandcode.commons.conf;

/**
 * Плагин загрузки conf
 */
public interface ConfLoaderPlugin {

    /**
     * Инициализация плагина
     *
     * @param loader для какого загрузчика
     */
    void initPlugin(ConfLoaderContext loader) throws Exception;

    /**
     * Возвращает значение для переменной #{var}.
     *
     * @param var имя переменной
     * @return значение переменной или null, если плагин не знает про такую перемнную
     */
    String getVar(String var) throws Exception;

    /**
     * Выполнить функцию.
     *
     * @param funcName имя функции
     * @param params   параметры функции
     * @param context  в контексте какого объекта нужно выполнить
     * @return true, если была выполнена, false - функция не известна плагину
     */
    boolean execFunc(String funcName, Conf params, Conf context) throws Exception;

    /**
     * Выполняется после полной загрузки
     */
    void afterLoad() throws Exception;

    /**
     * Вычислить выражение.
     * Используется в x-if и x-if-not.
     *
     * @param expr выражение
     * @return null, если не известное выражение
     */
    Object evalExpression(Conf expr);

}
