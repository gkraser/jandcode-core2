package jandcode.jc;

/**
 * Доступ к скриптам на низком уровне
 */
public interface IScripts {

    /**
     * По имени возвращает класс, наследник от {@link ProjectScript}
     *
     * @param scriptName либо полное имя класса, либо имя файла. Если файл не абсолютен,
     *                   то он будет рассматриваться относительно project, если project
     *                   указан
     * @param project    проект, для которого берется скрипт
     * @return класс или ошибка
     */
    Class getClassProjectScript(String scriptName, Project project);

    /**
     * Создать экземпляр IProjectScript по классу
     */
    IProjectScript createProjectScript(Class cls);

    /**
     * По классу возвращает файл, из которого был создан этот класс.
     * Если класс загружен не из файла, то возвращается null.
     *
     * @param cls класс
     * @return полное имя файла или null, если файла для скрипта нет
     */
    String getFileProjectScript(Class cls);

    /**
     * Возвращает файл с beforeLoad скрипт для указанного файла скрипта.
     * Этот скрипт выделяется из сигнатуры в тексте скрипта:
     * <pre>{@code
     * static beforeLoad = CLOSURE
     * }</pre>
     * Полученный класс наследник от {@link ProjectScript} и
     * реализует интерфейс {@link IBeforeLoadScript}.
     * closure становится методом {@link IBeforeLoadScript#executeBeforeLoad()}
     *
     * @param filename полное имя файла скрипта
     * @return null, если файл не содержит скрипта beforeLoad
     */
    String getBeforeLoadProjectScript(String filename);

}
