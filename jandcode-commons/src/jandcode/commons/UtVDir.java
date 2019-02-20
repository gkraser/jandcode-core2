package jandcode.commons;

import jandcode.commons.impl.ant.*;
import org.apache.commons.io.*;

import java.io.*;

/**
 * Утилиты для виртуальных каталогов.
 * Здесь путь интерпретируется как набор элементов разделенных '/'. Корневой каталог
 * определяется пустой строкой.
 */
public class UtVDir {

    /**
     * Нормализация строки, содержащей условный путь. Заменяются '\' на '/', убираются
     * слеши в начале и конце, '..' удаляются
     *
     * @param path путь
     * @return нормализованный путь
     */
    public static String normalize(String path) {
        if (path == null) {
            return "";
        }
        if (path.equals(".")) {
            return "";
        }
        if (path.contains("..")) {
            path = path.replace("..", "");
        }
        if (path.indexOf('\\') != -1) {
            path = path.replace("\\", "/");
        }
        while (path.contains("//")) {
            path = path.replace("//", "/");
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * Объединение путей
     *
     * @param path
     * @param childPaths
     * @return нормализованный объединенный путь
     */
    public static String join(String path, String... childPaths) {
        path = normalize(path);
        for (String childPath : childPaths) {
            String p = normalize(childPath);
            if (p.length() > 0) {
                if (path.length() > 0) {
                    path = path + "/";
                }
                path = path + p;
            }
        }
        return path;
    }

    /**
     * Объединение гарантированно нормализованных путей
     *
     * @return нормализованный объединенный путь
     */
    public static String joinNormalized(String path, String... childPaths) {
        for (String p : childPaths) {
            if (p.length() > 0) {
                if (path.length() > 0) {
                    path = path + "/";
                }
                path = path + p;
            }
        }
        return path;
    }

    /**
     * Быстрый join для 2-х нормализованных путей
     */
    public static String joinNormalized(String f1, String f2) {
        if (f1.length() == 0) {
            if (f2.length() == 0) {
                return "";
            }
            return f2;
        }
        if (f2.length() == 0) {
            return f1;
        }
        return f1 + "/" + f2;
    }

    /**
     * Для указанного пути возвращает true, если он относительный.
     * Это определяется по наличию './' в строке.
     */
    public static boolean isRelPath(String path) {
        if (UtString.empty(path)) {
            return false;
        }
        if (path.indexOf("./") != -1) {
            return true;
        }
        return false;
    }

    /**
     * Развернуть путь relPath в абсолютный, относительно каталога basePath
     *
     * @param basePath каталог, относительно которого ведутся расчеты
     * @param relPath  путь. Может быть относительным и абсолютным. Относительный определяется
     *                 функцией {@link UtVDir#isRelPath(java.lang.String)}
     * @return абсолютный путь
     */
    public static String expandRelPath(String basePath, String relPath) {
        if (!isRelPath(relPath)) {
            return normalize(relPath);
        }
        String s = basePath + "/" + relPath;
        s = FilenameUtils.normalize(s, true);
        if (UtString.empty(s)) {
            return "";
        }
        return normalize(s);
    }

    /**
     * Получить относительный путь
     *
     * @param basePath каталог, относительно которого ведутся расчеты
     * @param path     путь, который нужно превратить в относительный. Считается, что он абсолютный
     * @return абсолютный путь
     */
    public static String getRelPath(String basePath, String path) {
        try {
            basePath = normalize(basePath);
            path = normalize(path);
            String s = UtFile.getRelativePath(basePath, path);
            return s;
        } catch (Exception e) {
            return path;
        }
    }

    /**
     * Проверка, что виртуальный путь совпадает с маской.
     *
     * @param mask          маска в формате ant
     * @param path          путь
     * @param caseSensitive true - учитывать регистр
     */
    public static boolean matchPath(String mask, String path, boolean caseSensitive) {
        // реализация из ant...
        if (UtString.empty(mask) || UtString.empty(path)) {
            return false;
        }
        if (File.separatorChar != '/') {
            mask = mask.replace('/', File.separatorChar);
            path = path.replace('/', File.separatorChar);
        }
        return AntSelectorUtils.matchPath(mask, path, caseSensitive);
    }

    /**
     * Проверка, что виртуальный путь совпадает с маской (с учетом регистра).
     *
     * @param mask маска в формате ant
     * @param path путь
     */
    public static boolean matchPath(String mask, String path) {
        return matchPath(mask, path, true);
    }

}
