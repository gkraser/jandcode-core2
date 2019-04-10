package jandcode.jc;

import groovy.lang.*;
import jandcode.commons.named.*;

import java.util.*;

/**
 * Проект
 */
public interface Project extends INamed, IEvents {

    /**
     * Контекст
     */
    Ctx getCtx();

    /**
     * Имя проекта. По умолчанию - имя каталога, откуда он был загружен.
     */
    String getName();

    /**
     * Изменить имя проекта
     */
    void setName(String name);

    /**
     * Версия проекта. По умолчанию SNAPSHOT
     */
    IVersion getVersion();

    /**
     * Установить версию.
     *
     * @param v версия. Может быть null (сбросит в версию по умолчанию),
     *          строка (явная версия), Project (версия проекта), {@link IVersion}.
     */
    void setVersion(Object v);

    /**
     * Рабочий каталог проекта
     */
    Dir getWd();

    /**
     * Полный путь каталога path, заданного относительно рабочего каталога проекта.
     * Вызов {@link Dir#join(java.lang.CharSequence)}
     */
    String wd(String path);

    /**
     * Полный путь рабочего каталога проекта.
     * Вызов {@link Dir#getPath()}
     */
    String wd();

    /**
     * Полное имя файла, из которого был загружен проект.
     * Может физически не существовать. Путь этого файла совпадает с getWd()
     */
    String getProjectFile();

    /**
     * Возвращает корневой проект для этого проекта.
     * Т.е. проект выше по иерархии каталогов, в каталоге которого есть файл .jc-root.
     * <p>
     * Если сам проект является корневым, то возвращается null.
     *
     * @return null, если нет корневого проекта
     */
    Project getRootProject();

    //////

    /**
     * Map с набором переменных проекта.
     * Можно свободно писать и читать.
     */
    Vars getVars();

    ////// commands

    /**
     * Хранилище команд проекта
     */
    CmHolder getCm();

    ////// events

    /**
     * Добавляет обработчик события 'project-after-load'.
     * Выполняется после загрузки скрипта проекта
     */
    void afterLoad(Closure handler);

    /**
     * Добавляет обработчик события 'project-after-load-all'.
     * Выполняется после загрузки скрипта проекта и после того, как все afterLoad отработали
     */
    void afterLoadAll(Closure handler);

    //////

    /**
     * Выполнить скрипт в контексте объекта, если этот скрипт еще не выполнялся.
     * Возвращается экземпляр скрипта. Если скрипт был включен ранее, возвращается
     * ранее выполненный экземпляр.
     */
    IProjectScript include(String scriptName);

    /**
     * Если скрипт был включен, возвращает ссылку на него, иначе - null.
     */
    IProjectScript getIncluded(String scriptName);

    /**
     * Выполнить скрипт в контексте объекта, если этот скрипт еще не выполнялся.
     * Возвращается экземпляр скрипта. Если скрипт был включен ранее, возвращается
     * ранее выполненный экземпляр.
     */
    <A extends IProjectScript> A include(Class<A> scriptClass);

    /**
     * Если скрипт был включен, возвращает ссылку на него, иначе - null.
     */
    <A extends IProjectScript> A getIncluded(Class<A> scriptClass);

    /**
     * Список include, которые реализуют интерфейс clazz
     */
    <A extends Object> List<A> impl(Class<A> clazz);

    //////

    /**
     * Создать скрипт в контексте проекта.
     * Возвращается экземпляр скрипта.
     * Метод onInclude скрипта не выполняется.
     */
    IProjectScript create(String scriptName);

    /**
     * Создать скрипт в контексте проекта.
     * Возвращается экземпляр скрипта.
     * Метод onInclude скрипта не выполняется.
     */
    <A extends IProjectScript> A create(Class<A> scriptClass);

}
