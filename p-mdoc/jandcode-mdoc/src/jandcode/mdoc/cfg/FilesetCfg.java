package jandcode.mdoc.cfg;

import jandcode.commons.*;

import java.util.*;

public class FilesetCfg {

    private String dir;
    private String prefix;
    private boolean resources;
    private List<String> includes = new ArrayList<>();
    private List<String> excludes = new ArrayList<>();

    /**
     * vfs-путь до каталога с исходниками.
     */
    public String getDir() {
        if (dir == null) {
            return "";
        }
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * Префикс папки, в которую монтируются файлы.
     * По умолчанию - пусто(в корень)
     */
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Набор файлов содержит только ресурсы и не содержит статьи.
     */
    public boolean isResources() {
        return resources;
    }

    public void setResources(boolean resources) {
        this.resources = resources;
    }

    /**
     * Маски для включения (формат как в ant)
     */
    public List<String> getIncludes() {
        return includes;
    }

    /**
     * Маски для исключения  (формат как в ant)
     */
    public List<String> getExcludes() {
        return excludes;
    }

    //////

    public void addInclude(String mask) {
        getIncludes().add(mask);
    }

    public void addExclude(String mask) {
        getExcludes().add(mask);
    }

    private void assignMasks(List<String> res, String masks) {
        if (UtString.empty(masks)) {
            return;
        }
        List<String> lst = UtCnv.toList(masks);
        res.addAll(lst);
    }

    public void setIncludes(String s) {
        getIncludes().clear();
        assignMasks(getIncludes(), s);
    }

    public void setExcludes(String s) {
        getExcludes().clear();
        assignMasks(getExcludes(), s);
    }

}
