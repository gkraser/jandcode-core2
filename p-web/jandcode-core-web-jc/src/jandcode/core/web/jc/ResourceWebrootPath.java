package jandcode.core.web.jc;

import jandcode.commons.*;

import java.util.*;

/**
 * Описание пути для монтирования путей из ресурсов
 */
public class ResourceWebrootPath {

    private String realPath;
    private String virtualPath;
    private List<String> excludes = new ArrayList<>();

    public ResourceWebrootPath(String realPath) {
        this.realPath = realPath;
    }

    public ResourceWebrootPath(String realPath, String virtualPath) {
        this.realPath = realPath;
        this.virtualPath = virtualPath;
    }

    /**
     * Реальный путь
     */
    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    /**
     * Виртуальный путь
     */
    public String getVirtualPath() {
        return UtString.empty(virtualPath) ? "" : virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    /**
     * Список масок, которые будут исключены при сборке продукта
     */
    public List<String> getExcludes() {
        return excludes;
    }

}
