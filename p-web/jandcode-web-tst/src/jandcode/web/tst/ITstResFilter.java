package jandcode.web.tst;

import jandcode.web.virtfile.*;

/**
 * Фильтр для файлов в _tst и _tst/run
 */
public interface ITstResFilter {

    /**
     * Игнорировать ли этот файл
     *
     * @param f какой файл
     * @return true - файл не нужен
     */
    boolean isIgnore(VirtFile f);

}
