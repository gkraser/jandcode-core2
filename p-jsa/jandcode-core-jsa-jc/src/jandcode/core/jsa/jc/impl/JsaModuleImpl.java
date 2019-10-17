package jandcode.core.jsa.jc.impl;

import jandcode.commons.*;
import jandcode.commons.moduledef.*;
import jandcode.core.jsa.jc.*;
import jandcode.jc.*;
import jandcode.jc.std.*;

import java.util.*;
import java.util.jar.*;

public class JsaModuleImpl implements JsaModule {

    private ModuleDef moduleDef;
    private String srcPath;
    private Lib lib;
    private boolean jsaModule;
    private List<String> resolvePaths = new ArrayList<>();
    private Map<String, String> nodeDepends = new LinkedHashMap<>();
    private Map<String, Object> gulpTasks = new LinkedHashMap<>();

    public JsaModuleImpl(Ctx ctx, Lib lib) {
        this.lib = lib;
        if (lib.getSourceProject() != null) {
            initSource(lib);
        } else {
            initJar(ctx, lib);
        }
    }

    private void initSource(Lib lib) {
        Project p = lib.getSourceProject();

        JavaProject jm = p.getIncluded(JavaProject.class);
        if (jm == null) {
            return;  // не java-проект
        }
        JsaProject jsa = p.getIncluded(JsaProject.class);
        if (jsa == null) {
            return; // не jsa-проект
        }
        if (jm.getDirsSrc().size() == 0) {
            return; // нет исходников
        }
        if (jm.getModuleDefs().size() == 0) {
            return; // нет определений модулей
        }

        this.jsaModule = true;

        this.srcPath = p.wd(jm.getDirsSrc().get(0));
        this.moduleDef = jm.getModuleDefs().get(0);
        this.resolvePaths.add(this.srcPath);

        this.nodeDepends.putAll(jsa.getNodeDepends());
        this.gulpTasks.putAll(jsa.getGulpTasks());
    }

    @SuppressWarnings("unchecked")
    private void initJar(Ctx ctx, Lib lib) {
        if (lib.isSys()) {
            return; // системные даже не смотрим
        }
        String jar = lib.getJar();
        if (UtString.empty(jar)) {
            return;  // не jar
        }
        if (lib.getModules().size() == 0) {
            return; // нет модулей
        }

        JarCacheService jsvc = ctx.service(JarCacheService.class);

        Manifest manifest = jsvc.getManifest(jar);

        boolean jsaFlag = UtCnv.toBoolean(manifest.getMainAttributes().getValue(JsaConsts.MANIFEST_JSA_PROJECT));
        if (!jsaFlag) {
            return;  // не jsa модуль
        }

        // маркирем
        this.jsaModule = true;

        // это содержимое модуля
        this.srcPath = jsvc.getContentDir(jar);

        // создаем moduleDef
        String moduleDefName = lib.getModules().get(0);
        this.moduleDef = UtModuleDef.createModuleDef(
                moduleDefName,
                UtFile.join(this.srcPath, moduleDefName.replace('.', '/')),
                null,
                null
        );

        this.resolvePaths.add(this.srcPath);

        String sdata = UtCnv.toString(manifest.getMainAttributes().getValue(JsaConsts.MANIFEST_JSA_DATA));
        if (!UtString.empty(sdata)) {
            Map mdata = (Map) JsaUtJson.fromJson(sdata);
            Object ob;
            //
            ob = mdata.get("nodeDepends");
            if (ob instanceof Map) {
                this.nodeDepends.putAll((Map) ob);
            }
            //
            ob = mdata.get("gulpTasks");
            if (ob instanceof Map) {
                this.gulpTasks.putAll((Map) ob);
            }
        }

    }

    //////

    /**
     * Является ли правильным jsa-модулем
     */
    public boolean isJsaModule() {
        return jsaModule;
    }

    //////

    public String getName() {
        return lib.getName();
    }

    public ModuleDef getModuleDef() {
        return moduleDef;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public Lib getLib() {
        return lib;
    }

    public List<String> getResolvePaths() {
        return resolvePaths;
    }

    public Map<String, String> getNodeDepends() {
        return nodeDepends;
    }

    public Map<String, Object> getGulpTasks() {
        return gulpTasks;
    }

}
