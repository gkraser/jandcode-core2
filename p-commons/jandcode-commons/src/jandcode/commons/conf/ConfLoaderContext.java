package jandcode.commons.conf;

import java.util.*;

/**
 * Контекст загрузки
 */
public interface ConfLoaderContext extends ConfLoader {

    /**
     * Стандартный include.
     *
     * @param dest     куда
     * @param path     путь файла (может быть с '*')
     * @param required путь должен существовать
     */
    void includePath(Conf dest, String path, boolean required) throws Exception;

    /**
     * Зарегистрированные плагины
     */
    Collection<ConfLoaderPlugin> getPlugins();

    /**
     * Возвращает абсолютный путь для текущего состояния загрузчика
     * в формате vfs.
     *
     * @param path для какого относительного. Если он абсолютный, то он и возвращается.
     */
    String getAbsPath(String path);

    /**
     * Раскрыть все переменные
     *
     * @param s исходная строка с переменными
     */
    String expandVars(String s);

    /**
     * Выполнить функцию.
     *
     * @param funcName имя функции
     * @param params   параметры функции
     * @param context  в контексте какого объекта нужно выполнить
     */
    void execFunc(String funcName, Conf params, Conf context);

    /**
     * Добавить информацию о том, откуда взялось свойство.
     *
     * @param conf    для какого объекта
     * @param prop    какое свойство. Может быть null, тогда определяет что сам
     *                conf определен в этом месте.
     * @param lineNum номер строки текущего файла, где оно определено
     */
    void addOrigin(Conf conf, String prop, int lineNum);

}
