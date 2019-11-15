package jandcode.core.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.std.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;

import java.util.*;
import java.util.concurrent.*;

public class JsModuleServiceImpl extends BaseComp implements JsModuleService, ICheckChangedResource {

    private Map<String, JsModule> modules = new ConcurrentHashMap<>();
    private Map<String, JsModule> modulesById = new ConcurrentHashMap<>();
    private ModuleNameResolver moduleNameResolver;
    private List<JsModuleFactory> moduleFactorys = new ArrayList<>();
    private NamedList<ModuleCfg> moduleCfgs = new DefaultNamedList<>();
    private Map<String, String> moduleMapping = new HashMap<>();

    class ModuleCfg extends Named {
        Conf conf;

        public ModuleCfg(Conf conf) {
            setName(UtConf.getNameAsPath(conf));
            this.conf = conf;
        }

        public Conf getConf() {
            return conf;
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.moduleNameResolver = new ModuleNameResolver(this);

        Conf webRt = getApp().getConf().getConf("web");

        // factorys
        for (Conf r : webRt.getConfs("jsmodule-factory")) {
            JsModuleFactoryImpl ft = getApp().create(r, JsModuleFactoryImpl.class);
            moduleFactorys.add(0, ft);
        }

        // mapping
        for (Conf r : webRt.getConfs("jsmodule-mapping")) {
            String src = r.getString("src");
            String dest = r.getString("dest");
            if (UtString.empty(src)) {
                throw new XError("src empty in jsmodule-mapping: {0}", r.origin());
            }
            if (UtString.empty(dest)) {
                throw new XError("dest empty in jsmodule-mapping: {0}", r.origin());
            }
            this.moduleMapping.put(src, dest);
        }

        // module cfg
        for (Conf r : webRt.getConfs("jsmodule")) {
            ModuleCfg mcfg = new ModuleCfg(r);
            this.moduleCfgs.add(mcfg);
        }

    }

    public List<String> resolveRequire(String basePath, String require) {
        String p = require;
        if (UtVDir.isRelPath(p)) {
            p = UtVDir.expandRelPath(basePath, p);
        }
        List<String> lst = UtWeb.expandPath(getApp(), p);
        List<String> res = new ArrayList<>();
        for (String mp : lst) {
            res.add(resolveModuleName(mp));
        }
        return res;
    }

    public String resolveModuleName(String name) {
        String mappingName = this.moduleMapping.get(name);
        if (mappingName == null) {
            return this.moduleNameResolver.resolveModuleName(name);
        } else {
            return this.moduleNameResolver.resolveModuleName(mappingName);
        }
    }

    public List<JsModule> getModules(String paths) {
        List<JsModule> res = new ArrayList<>();
        if (UtString.empty(paths)) {
            return res;
        }

        String[] names = paths.split(",");
        for (String nm : names) {
            if (UtString.empty(nm)) {
                continue;
            }
            List<String> reqs = resolveRequire("", nm);
            for (String mn : reqs) {
                String mn1 = resolveModuleName(mn);
                JsModule m = getModule(mn1);
                res.add(m);
            }
        }

        return res;
    }

    public JsModule getModule(String path) {
        JsModule m = findModule(path);
        if (m == null) {
            throw new XError("Модуль [{0}] не найден", path);
        }
        return m;
    }

    public JsModule findModule(String path) {
        path = UtVDir.normalize(path);

        JsModule m = modules.get(path);
        if (m == null) {
            VirtFile f = getApp().bean(WebService.class).findFile(path);
            if (f != null) {
                synchronized (this) {
                    m = modules.get(path);
                    if (m == null) {
                        m = createModule(f);
                        modules.put(path, m);
                        modulesById.put(m.getId(), m);
                    }
                }
            }
        }
        return m;

    }

    public JsModule findModuleById(String id) {
        return this.modulesById.get(id);
    }

    public JsModule getModuleById(String id) {
        JsModule m = findModuleById(id);
        if (m == null) {
            throw new XError("Module [{0}] not found", id);
        }
        return m;
    }

    private JsModule createModule(VirtFile f) {
        String moduleName = f.getPath();
        for (JsModuleFactory fc : this.moduleFactorys) {
            if (fc.isCanCreate(moduleName)) {
                Conf rtCfg = null;
                ModuleCfg modCfg = this.moduleCfgs.find(moduleName);
                if (modCfg != null) {
                    rtCfg = modCfg.getConf();
                }
                return fc.createInst(f, rtCfg);
            }
        }
        throw new XError("Файл не является модулем [{0}]", moduleName);
    }

    public boolean isModule(String path) {
        for (JsModuleFactory fc : this.moduleFactorys) {
            if (fc.isCanCreate(path)) {
                return true;
            }
        }
        return false;
    }

    public void checkChangedResource(CheckChangedResourceInfo info) throws Exception {
        // простая реализация: считаем что в production состав файлов не меняется вообще
        // в debug - если хоть один asset изменился - просто очищаем кеш
        if (getApp().getEnv().isDev() && !getApp().getEnv().isTest()) {
            boolean mod = false;
            for (JsModule a : modules.values()) {
                if (a.isModified()) {
                    mod = true;
                    break;
                }
            }
            if (mod) {
                synchronized (this) {
                    modules = new ConcurrentHashMap<>();
                    modulesById = new ConcurrentHashMap<>();
                }
                //getApp().fireEvent(new Event_CacheInvalidate());
            }
        }
    }

}
