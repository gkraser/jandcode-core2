package jandcode.commons.io;

import java.util.*;

/**
 * Универсальный и простой сканер файловой системы.
 * В качестве масок используются маски, аналогичные ant.
 */
public interface DirScanner<TYPE> {

    /**
     * Установить корневой каталог сканирования. Возможно указывать маску.
     * Например: 'd:\t', 'd:\t\*.java', 'd:\t\**\*.java'.
     * <p>
     * root разделяется на 2 части: путь и маску. Если маска явно не указана,
     * подразумевается '**\*'.
     * <pre>{@code
     * d:\t\*.java        -> path=d:\t, mask=*.java
     * d:\t\**\*.java     -> path=d:\t, mask=**\*.java
     * d:\t               -> path=d:\t, mask=**\*
     * }</pre>
     * Перед выполнением очищается список include.
     */
    void setDir(String dir);

    /**
     * Включить маску
     */
    void include(String mask);

    /**
     * Исключить маску
     */
    void exclude(String mask);

    /**
     * Нужны ли файлы. По умолчанию - true.
     */
    void setNeedFiles(boolean needFiles);

    /**
     * Нужны ли каталоги. По умолчанию - false.
     */
    void setNeedDirs(boolean needDirs);

    /**
     * Загрузить все
     */
    List<TYPE> load();

    /**
     * Сканировать
     *
     * @param callback вызывать для каждого найденного
     */
    void scan(DirScannerCallback<TYPE> callback);

}
