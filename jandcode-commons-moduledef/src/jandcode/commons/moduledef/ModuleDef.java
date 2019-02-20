package jandcode.commons.moduledef;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Описание модуля.
 * Представление пакета особым способом для jandcode.
 * <p>
 * Описание модуля существует вне приложения, само по себе.
 * <p>
 * Именем модуля является имя его java-пакета.
 */
public interface ModuleDef extends INamed {

    /**
     * Путь до каталога с модулем. Формат VFS.
     * Этот каталог обычно содержит файл module.cfx.
     */
    String getPath();

    /**
     * Файл с конфигурацией модуля.
     * Это файл module.cfx.
     */
    String getModuleFile();

    /**
     * Корневой пакет модуля.
     * Каждый модуль должен иметь уникальный корневой пакет.
     * Обчно имеет такое же значение, как и имя модуля.
     */
    String getPackageRoot();

    /**
     * Набор произвольных свойств этого определения модуля
     */
    Map<String, Object> getProps();

    /**
     * Получить информацию об исходниках модуля.
     * Если модуль не в исходниках, все равно возвращается экземпляр.
     */
    ModuleDefSourceInfo getSourceInfo();

    /**
     * Список зависимостей модуля.
     * Это список имен модулей.
     */
    List<String> getDepends();

}
