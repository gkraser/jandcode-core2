package jandcode.core.dbm.verdb;

/**
 * Файл с набором changeset.
 * Состоит из набора операторов.
 */
public interface VerdbFile extends IVerdbVersionLink {

    /**
     * Полный vfs-путь до файла
     */
    String getPath();

}
