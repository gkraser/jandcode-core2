package jandcode.commons.vdir.impl;

import jandcode.commons.vdir.*;

public class CustomVRoot implements VRoot {

    private String rootPath;
    private String prefix;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isSame(String rootPath, String prefix) {
        return this.rootPath.equals(rootPath) && this.prefix.equals(prefix);
    }

    public boolean isEmpty() {
        return prefix.length() == 0;
    }

    public String prepareVPath(String path1) {
        if (!isEmpty()) {
            if (path1.equals(prefix)) {
                return "";
            }
            if (!path1.startsWith(prefix + "/")) {
                // префикс есть. Путь с префикса не начинается. Пропускаем
                return null;
            }
            // удаляем префикс из запрашиваемого пути
            path1 = path1.substring(prefix.length() + 1);
        }
        return path1;
    }

    /**
     * Для пути path возвращает dummy-папку.
     * Например: префикс у каталога aaa/sss/ddd, передаем путь aaa, получаем sss
     *
     * @return null, если нет
     */
    public String getDummyFolder(String path) {
        if (prefix.startsWith(path + "/")) {
            String dd = prefix.substring(path.length() + 1);
            String[] ar = dd.split("/");
            if (ar.length > 0) {
                return ar[0];
            }
        }
        return null;
    }

    public String getRootPrefixFolder() {
        if (prefix.length() == 0) {
            return null;
        }
        String[] ar = prefix.split("/");
        if (ar.length > 0) {
            return ar[0];
        }
        return null;
    }

}
