package jandcode.commons.moduledef.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import java.util.*;

public class ModuleDefResolverImpl implements ModuleDefResolver {

    protected static Logger log = LoggerFactory.getLogger(ModuleDefResolverImpl.class);

    // модули в исходниках
    private NamedList<ModuleDef> sourceModules = new DefaultNamedList<>();
    private boolean sourceModulesLoaded;

    private NamedList<ModuleDef> cachedItems = new DefaultNamedList<>();

    public ModuleDef findModuleDef(String moduleName) {
        ModuleDef res = cachedItems.find(moduleName);
        if (res == null) {
            res = findModuleDefInResources(moduleName);
            if (res != null) {
                if (res.getModuleFile().startsWith("file:/")) {
                    // модуль в исходниках
                    ModuleDef resSrc = sourceModules.find(res.getName());
                    if (resSrc == null) {
                        if (sourceModulesLoaded) {
                            throw new XError("Модуль {0} [{1}] в исходниках, но для него " +
                                    "не найдено описание исходников в файле {2}. " +
                                    "Необходимо выполнить jc prepare в корневом проекте.",
                                    res.getName(), res.getPath(), ModuleDefConsts.FILE_REGISTRY_MODULE_DEF
                            );
                        } else {
                            throw new XError("Модуль {0} [{1}] в исходниках, но загрузка " +
                                    "файла {2} не была произведена. Укажите среду {3}=true в файле {4}.",
                                    res.getName(), res.getPath(),
                                    ModuleDefConsts.FILE_REGISTRY_MODULE_DEF,
                                    UtilsConsts.PROP_ENV_SOURCE, UtilsConsts.FILE_ENV
                            );

                        }
                    }
                    res = resSrc;
                }
                cachedItems.add(res);
            }
        }
        return res;
    }

    public ModuleDef getModuleDef(String moduleName) {
        ModuleDef md = findModuleDef(moduleName);
        if (md == null) {
            ModuleDef resSrc = sourceModules.find(moduleName);
            if (resSrc != null) {
                throw new XError("Модуль {0} не найден в classpath. Модуль описан в [{2}] с корнем в [{1}]",
                        moduleName, resSrc.getPath(), ModuleDefConsts.FILE_REGISTRY_MODULE_DEF
                );
            } else {
                throw new XError("Модуль {0} не найден", moduleName);
            }
        }
        return md;
    }

    public void addWorkDir(String workDir) {
        String f = UtFile.join(workDir, ModuleDefConsts.FILE_REGISTRY_MODULE_DEF);
        if (UtFile.exists(f)) {
            try {
                log.info("load module defs from " + f);
                List<ModuleDef> tmp = ModuleDefUtilsImpl.loadModuleDefsFromConfFile(f);
                sourceModules.addAll(tmp);
                sourceModulesLoaded = true;
                log.info("load module defs from path: ok");
            } catch (Exception e) {
                throw new XErrorMark(e, "file: " + f);
            }
        }
    }

    //////

    protected ModuleDef findModuleDefInResources(String moduleName) {
        FileObject f;
        FileObject fp;
        try {
            String mf = "res:" + moduleName.replace(".", "/") + "/" + ModuleDefConsts.FILE_MODULE_CONF;
            f = UtFile.getFileObject(mf);
            if (!f.exists()) {
                return null;
            }
            fp = f.getParent();
        } catch (Exception e) {
            return null;
        }
        return UtModuleDef.createModuleDef(moduleName, fp.toString(), null, f.toString());
    }

}
