package jandcode.core.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.web.virtfile.*;

import java.util.*;

public abstract class JsModuleImpl extends BaseComp implements JsModule {

    private JsModuleService svc;
    private VirtFile file;
    private String id;
    private String text;
    private String hash;
    private List<RequireItem> requires = new ArrayList<>();
    private List<JsModule> requiresExpanded;
    private long createTime;
    private List<VirtFile> modifyDepends = new ArrayList<>();
    private boolean dynamic;
    private Conf conf;

    /**
     * Инициализация
     *
     * @param file из какого файла
     * @param conf конфигурация модуля
     */
    public void init(VirtFile file, Conf conf) {
        try {
            UtReflect.getUtils().setProps(this, conf);

            this.createTime = System.currentTimeMillis();
            this.svc = getApp().bean(JsModuleService.class);
            this.file = file;
            this.setName(file.getPath());
            this.id = JsModuleUtils.pathToId(this.getName());
            //
            JsModuleBuilderImpl mb = new JsModuleBuilderImpl(this, this.svc, conf);
            onInit(mb);
            //
            this.text = mb.getText();
            this.requires = mb.getRequires();
            this.modifyDepends = mb.getModifyDepends();
            this.dynamic = mb.isDynamic();
            if (this.dynamic) {
                this.conf = mb.getConf();
            }
            //
        } catch (Exception e) {
            throw new XErrorMark(e, "file: " + this.file.getPath());
        }
    }

    /**
     * Инициализация модуля
     *
     * @param b построитель модуля
     */
    protected abstract void onInit(JsModuleBuilder b) throws Exception;

    public String toString() {
        return getName();
    }

    /**
     * Какой файл используется как модуль
     */
    public VirtFile getFile() {
        return file;
    }

    /**
     * Время создания экземпляра
     */
    protected long getCreateTime() {
        return createTime;
    }

    /**
     * Ссылка на сервис модулей
     */
    protected JsModuleService getSvc() {
        return svc;
    }

    //////

    public String getId() {
        return id;
    }

    public String getText() {
        if (isDynamic()) {
            JsModuleBuilderImpl mb = new JsModuleBuilderImpl(this, this.svc, this.conf);
            try {
                onInit(mb);
            } catch (Exception e) {
                throw new XErrorMark(e, "module:" + getName());
            }
            //
            return mb.getText();
        }
        return this.text;
    }

    public List<RequireItem> getRequires() {
        return this.requires;
    }

    public boolean isModified() {
        for (VirtFile f : this.modifyDepends) {
            if (f.getLastModTime() > getCreateTime()) {
                return true;
            }
        }
        return false;
    }

    public String getHash() {
        if (hash == null) {
            synchronized (this) {
                if (hash == null) {
                    if (isDynamic()) {
                        hash = "";
                    } else {
                        hash = UtString.md5Str(getText());
                    }
                }
            }
        }
        return hash;
    }

    //////

    public List<JsModule> getRequiresExpanded() {
        if (requiresExpanded == null) {
            synchronized (this) {
                if (requiresExpanded == null) {
                    requiresExpanded = expandRequires();
                }
            }
        }
        return requiresExpanded;
    }

    protected List<JsModule> expandRequires() {
        RequiresExpander expander = new RequiresExpander(getSvc());

        for (RequireItem ri : getRequires()) {
            for (String pt : ri.getRealNames()) {
                expander.addRequire(this, pt);
            }
        }

        return expander.getItems();
    }

    public boolean isDynamic() {
        return dynamic;
    }

}
