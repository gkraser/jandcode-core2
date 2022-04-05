package jandcode.core.dbm.verdb;

import java.util.*;

/**
 * Каталог с набором changeset.
 * Состоит из набора файлов.
 */
public interface VerdbDir extends IVerdbVersionLink {

    /**
     * Полный vfs-путь до каталога
     */
    String getPath();

    /**
     * Файлы в каталоге
     */
    List<VerdbFile> getFiles();

}
