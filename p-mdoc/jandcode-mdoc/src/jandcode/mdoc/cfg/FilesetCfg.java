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
     * Установить свойства из map
     */
    public void setProps(Map props) {
        if (props == null) {
            return;
        }
        Object v;

        //
        v = props.get("dir");
        if (v != null) {
            setDir(UtCnv.toString(v));
        }

        //
        v = props.get("prefix");
        if (v != null) {
            setPrefix(UtCnv.toString(v));
        }

        //
        v = props.get("resources");
        if (v != null) {
            setResources(UtCnv.toBoolean(v));
        }

        //
        v = props.get("includes");
        if (v != null) {
            getIncludes().clear();
            getIncludes().addAll(UtCnv.toList(v));
        }

        //
        v = props.get("excludes");
        if (v != null) {
            getExcludes().clear();
            getExcludes().addAll(UtCnv.toList(v));
        }
    }

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

}
