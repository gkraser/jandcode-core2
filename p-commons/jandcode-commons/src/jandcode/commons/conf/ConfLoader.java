package jandcode.commons.conf;

import jandcode.commons.io.*;

import java.util.*;

/**
 * Интерфейс загрузчика Conf
 */
public interface ConfLoader extends ILoader {

    /**
     * Загружаемая конфигурация
     */
    Conf getRoot();

    /**
     * Регистрация экземпляра плагина перед загрузкой.
     *
     * @param pluginInst экземпляр плагина
     */
    void registerPlugin(ConfLoaderPlugin pluginInst);

    /**
     * Загруженные файлы. Уникальный список всех файлов, которые учавствовали в загрузке.
     */
    Collection<String> getLoadedFiles();


}
