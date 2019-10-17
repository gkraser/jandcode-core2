package jandcode.core.web.virtfile;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.impl.virtfs.*;

/**
 * Базовый класс для точек монтирования
 */
public abstract class BaseMount extends BaseComp implements Mount {

    private String virtualPath = "";

    /**
     * Виртуальный путь, куда монтируется
     */
    public String getVirtualPath() {
        return virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = UtVDir.normalize(virtualPath);
    }

    public String getRealPath() {
        return "";
    }

    /**
     * Создание экземпляра точки монтирования на реальный каталог.
     * Используется в провайдерах.
     */
    protected Mount createMountVfs(String name, String virtualPath, String realPath) {
        MountVfs m = getApp().create(MountVfs.class);
        m.setName(name);
        m.setVirtualPath(UtVDir.normalize(virtualPath));
        m.setRealPath(realPath);
        return m;
    }

}
