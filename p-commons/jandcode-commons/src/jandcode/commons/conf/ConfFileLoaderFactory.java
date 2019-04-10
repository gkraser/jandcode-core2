package jandcode.commons.conf;

import jandcode.commons.io.*;

/**
 * Фабрика создания загрузчиков Conf из файла.
 */
public interface ConfFileLoaderFactory {

    /**
     * Создать loader для указанного типа файла.
     * Обычно по расширению.
     *
     * @param extension    расширение файла
     * @param root         для какой конфигураци
     * @param loaderConext для какого контекста загрузки
     * @return ILoader или null, если тип файла неизвестен
     */
    ILoader createFileLoader(String extension, Conf root, ConfLoaderContext loaderConext);

}
