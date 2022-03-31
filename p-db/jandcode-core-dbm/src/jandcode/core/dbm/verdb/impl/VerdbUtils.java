package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;

/**
 * Утилиты всякие для verdb
 */
public class VerdbUtils {

    /**
     * Извлечь версию из имени файла.
     * Формат имени 'NNN-[xxx]', где NNN - извлекаемая версия.
     *
     * @param fn имя файла
     * @return -1, если не удалось
     */
    public static long extractVersionFromFilename(String fn) {
        if (UtString.empty(fn)) {
            return -1;
        }
        int a = fn.indexOf('-');
        if (a != -1) {
            fn = fn.substring(0, a);
        }
        a = fn.indexOf('.');
        if (a != -1) {
            fn = fn.substring(0, a);
        }
        long res = UtCnv.toLong(fn, -1);
        return res;
    }


}
