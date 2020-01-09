package jandcode.core;

import jandcode.commons.conf.*;
import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;

import java.util.*;

/**
 * Модуль. Представляет собой ссылку на экземпляр ModuleDef адаптированной для
 * работы в рамках приложений.
 */
public interface ModuleInst extends INamed, IAppLink, IConfLink {

    /**
     * Имя модуля
     */
    String getName();

    /**
     * Корневой пакет модуля.
     * Каждый модуль должен иметь уникальный корневой пакет.
     */
    String getPackageRoot();

    /**
     * Каталог модуля (формат vfs)
     */
    String getPath();

    /**
     * Виртуальный каталог модуль - имя модуля, где точка заменена на слеш
     */
    String getVPath();

    /**
     * Конфигурация модуля
     */
    Conf getConf();

    /**
     * Набор произвольных свойств этого экземпляра модуля.
     * Можно свободно писать и читать.
     */
    Map<String, Object> getProps();

    /**
     * Список зависимостей модуля.
     * Это список имен модулей.
     */
    List<String> getDepends();

    /**
     * Получить информацию об исходниках модуля.
     * Если модуль не в исходниках, все равно возвращается экземпляр.
     */
    ModuleDefSourceInfo getSourceInfo();

}
