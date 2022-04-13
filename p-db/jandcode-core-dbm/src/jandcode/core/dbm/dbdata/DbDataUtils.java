package jandcode.core.dbm.dbdata;

import jandcode.commons.*;

/**
 * Утилитки для dbdata
 */
public class DbDataUtils {

    /**
     * Извлечение имени домена из имени файла: первое слово до '-' или до '.'.
     */
    public static String fileNameToDomainName(String filename) {
        String fn = UtFile.filename(filename);

        // убираем расширения
        int a = fn.indexOf('.');
        if (a != -1) {
            fn = fn.substring(0, a);
        }

        // убираем после '-'
        a = fn.indexOf('-');
        if (a != -1) {
            fn = fn.substring(0, a);
        }

        return fn;
    }

    /**
     * Извлечение имени StoreLoader из имени файла.
     * Это либо расширение, либо второе расширение, если из 2 и более.
     */
    public static String fileNameToStoreLoaderName(String filename) {
        String fn = UtFile.filename(filename);

        String ext = UtFile.ext(fn);
        if (UtString.empty(ext)) {
            return fn;
        }
        fn = UtFile.removeExt(fn);
        String ext2 = UtFile.ext(fn);
        if (!UtString.empty(ext2)) {
            return ext2;
        }
        return ext;
    }


}
