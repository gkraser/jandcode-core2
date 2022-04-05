package jandcode.core.dbm.verdb;

import java.util.*;

/**
 * Файл с набором changeset.
 * Состоит из набора операторов.
 */
public interface VerdbFile extends IVerdbVersionLink {

    /**
     * Какому каталогу принадлежит
     */
    VerdbDir getDir();

    /**
     * Полный vfs-путь до файла
     */
    String getPath();

    /**
     * Операторы в файле
     */
    List<VerdbOper> getOpers();

    /**
     * Последняя версия в файле.
     * Это версия последнего оператора в файле.
     */
    VerdbVersion getLastVersion();

}
