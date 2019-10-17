package jandcode.core.web.virtfile.impl.virtfs;

import jandcode.commons.*;

/**
 * Взаимоотношения префикса и пути для виртуальных каталогов.
 * Используется только для непустого префикса, путь может быть пустым.
 * И префикс и путь должны быть нормализованы.
 */
public class PrefixVsPath {

    /**
     * Префикс и путь не совпадают.
     */
    public static final int REL_NO = 0;

    /**
     * Префикс и путь одинаковые.
     * В этом случае virtualPath становится равным последнему элементу пути.
     * prefix       : a/b/c
     * path         : a/b/c
     * virtualPath  : c
     */
    public static final int REL_EQUAL = 1;

    /**
     * Путь начинается с префикса.
     * В этом случае virtualPath становится равным пути без префикса.
     * prefix       : a/b
     * path         : a/b/c/d
     * virtualPath  : c/d
     */
    public static final int REL_PATH_STARTSWITH_PREFIX = 2;

    /**
     * Префикс начинается с пути.
     * В этом случае virtualPath становится равным последнему элементу пути,
     * а virtualPath2 становится равным первому элементу префикса, если из него
     * удалить путь.
     * prefix       : a/b/c/d
     * path         : a/b
     * virtualPath  : b
     * virtualPath2 : c
     */
    public static final int REL_PREFIX_STARTSWITH_PATH = 3;

    /**
     * Путь пустой.
     * В этом случае virtualPath становится равным первому элементу префикса.
     * prefix       : a/b/c
     * path         :
     * virtualPath  : a
     */
    public static final int REL_PATH_EMPTY = 4;

    private int rel;
    private String virtualPath;
    private String virtualPath2 = "";

    public PrefixVsPath(String prefix, String path) {
        if (prefix.equals(path)) {
            // пути равны
            rel = REL_EQUAL;
            String[] a = path.split("/");
            virtualPath = a[a.length - 1];

        } else if (path.startsWith(prefix + "/")) {
            // путь начинается с префикса
            rel = REL_PATH_STARTSWITH_PREFIX;
            virtualPath = path.substring(prefix.length() + 1);

        } else if (UtString.empty(path)) {
            // путь пустой
            rel = REL_PATH_EMPTY;
            String[] a = prefix.split("/");
            virtualPath = a[0];

        } else if (prefix.startsWith(path + "/")) {
            // префикс начинается с пути
            rel = REL_PREFIX_STARTSWITH_PATH;
            String[] a = path.split("/");
            virtualPath = a[a.length - 1];
            String s = prefix.substring(path.length() + 1);
            a = s.split("/");
            virtualPath2 = a[0];
        }
    }

    /**
     * Отношения префикса и пути. см REL_xxx
     */
    public int getRel() {
        return rel;
    }

    /**
     * Виртуальный путь
     */
    public String getVirtualPath() {
        return virtualPath;
    }

    /**
     * Виртуальный путь 2
     */
    public String getVirtualPath2() {
        return virtualPath2;
    }

}
