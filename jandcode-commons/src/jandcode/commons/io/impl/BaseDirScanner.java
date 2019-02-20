package jandcode.commons.io.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;

import java.util.*;

public abstract class BaseDirScanner<TYPE> implements DirScanner<TYPE> {

    private String dir = "";
    private boolean needFiles = true;
    private boolean needDirs;
    private List<String> includes = new ArrayList<>();
    private List<String> excludes = new ArrayList<>();
    private boolean caseSensitive;
    private int recursiveDepth;

    public void setDir(String dir) {
        includes.clear();
        String[] m = UtFile.splitPathAndMask(dir, null);
        this.dir = m[0];
        if (m[1] != null) {
            include(m[1]);
        }
    }

    public void include(String mask) {
        this.includes.add(UtVDir.normalize(mask));
    }

    public void exclude(String mask) {
        this.excludes.add(UtVDir.normalize(mask));
    }

    public void setNeedFiles(boolean needFiles) {
        this.needFiles = needFiles;
    }

    public void setNeedDirs(boolean needDirs) {
        this.needDirs = needDirs;
    }

    private boolean isMatch(String fp, List<String> masks) {
        for (String mask : masks) {
            if (UtVDir.matchPath(mask, fp, caseSensitive)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchIncludes(String fp) {
        if (includes.size() == 0) {
            return true;
        }
        return isMatch(fp, includes);
    }

    private boolean isMatchExcludes(String fp) {
        return isMatch(fp, excludes);
    }

    //////

    public List<TYPE> load() {
        List<TYPE> res = new ArrayList<>();
        scan(file -> {
            res.add(file);
        });
        return res;
    }

    private int countFasets(String s) {
        int res = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '/') {
                res++;
            }
        }
        return res;
    }

    public void scan(DirScannerCallback<TYPE> callback) {
        if (!needDirs && !needFiles) {
            return;
        }
        if (callback == null) {
            return;
        }
        if (includes.size() == 0) {
            recursiveDepth = 10000;
        } else {
            recursiveDepth = 1;
            for (String inc : includes) {
                if (inc.contains("**")) {
                    recursiveDepth = 10000;
                    break;
                } else {
                    recursiveDepth = Math.max(recursiveDepth, countFasets(inc));
                }
            }
        }
        doScan(callback);
    }


    private void doScan(DirScannerCallback<TYPE> callback) {
        TYPE root = createItem(dir);
        if (root == null) {
            return;  // нет ничего вообще
        }
        if (isFile(root)) {
            return;
        }
        caseSensitive = isCaseSensitive(root);
        doScan(root, "", callback, 1);
    }

    private void doScan(TYPE root, String basename, DirScannerCallback<TYPE> callback, int level) {
        if (basename.length() > 0) {
            basename = basename + "/";
        }
        List<TYPE> lst = listDir(root);
        if (lst == null || lst.size() == 0) {
            // тут ничего нет
            return;
        }

        // файлы
        if (needFiles) {
            for (TYPE f : lst) {
                if (!isFile(f)) {
                    continue;
                }
                String fp = basename + getName(f);
                if (isMatchIncludes(fp) && !isMatchExcludes(fp)) {
                    callback.nextFile(f);
                }
            }
        }

        // каталоги
        if (level <= recursiveDepth || needDirs) {
            for (TYPE f : lst) {
                if (isFile(f)) {
                    continue;
                }
                String fp = basename + getName(f);
                if (isMatchExcludes(fp)) {
                    continue;
                }
                if (needDirs) {
                    if (isMatchIncludes(fp)) {
                        callback.nextFile(f);
                    }
                }
                if (level < recursiveDepth) {
                    doScan(f, fp, callback, level + 1);
                }
            }
        }

    }

    ////// abstract

    /**
     * Создать объект по пути
     */
    protected abstract TYPE createItem(String dir);

    /**
     * Файл?
     */
    protected abstract boolean isFile(TYPE f);

    /**
     * Отсортрованный по имени список дочерних файлов и каталогов. null - если нету.
     */
    protected abstract List<TYPE> listDir(TYPE f);

    /**
     * Имя
     */
    protected abstract String getName(TYPE f);

    /**
     * Возвращает true, если файловая система для f - рещистрозависимая.
     */
    protected boolean isCaseSensitive(TYPE f) {
        return true;
    }

}
