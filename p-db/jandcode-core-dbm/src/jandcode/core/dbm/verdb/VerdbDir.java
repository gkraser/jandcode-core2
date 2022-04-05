package jandcode.core.dbm.verdb;

import java.util.*;

/**
 * Каталог с набором changeset.
 * Состоит из набора файлов.
 */
public interface VerdbDir extends IVerdbVersionLink {

    /**
     * Какому модулю принадлежит
     */
    VerdbModule getModule();

    /**
     * Полный vfs-путь до каталога
     */
    String getPath();

    /**
     * Файлы в каталоге
     */
    List<VerdbFile> getFiles();

    /**
     * Последняя версия в каталоге.
     * Это версия последнего оператора в последнем файле.
     */
    VerdbVersion getLastVersion();

}
