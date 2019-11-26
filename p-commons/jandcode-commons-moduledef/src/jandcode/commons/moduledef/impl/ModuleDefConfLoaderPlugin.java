package jandcode.commons.moduledef.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.moduledef.*;
import org.apache.commons.vfs2.*;

import java.util.*;
import java.util.regex.*;

/**
 * Плагин для поддержки загрузки module.cfx
 */
public class ModuleDefConfLoaderPlugin extends BaseConfLoaderPlugin {

    private static Pattern P_MODULE = Pattern.compile("module\\.(.*)");

    private ModuleDef moduleDef;
    private ModuleDefResolver moduleDefResolver;
    private ModuleDefConfig config;
    private Map<String, String> vars;

    public ModuleDefConfLoaderPlugin(ModuleDef moduleDef, ModuleDefConfig config,
            ModuleDefResolver moduleDefResolver, Map<String, String> vars) {
        this.moduleDef = moduleDef;
        this.moduleDefResolver = moduleDefResolver;
        this.config = config;
        this.vars = vars;
    }

    public void initPlugin(ConfLoaderContext loader) throws Exception {
        super.initPlugin(loader);
        //
        if (this.vars != null) {
            loader.getVars().putAll(this.vars);
        }
    }

    public void afterLoad() throws Exception {
        super.afterLoad();
        //
        this.config.getConfVars().putAll(getLoader().getVars());
    }

    /**
     * Текущий путь, относительно корня модуля
     */
    protected String getRelPath(ModuleDef moduleDef) {
        FileObject fc = UtFile.getFileObject(getLoader().getAbsPath(""));
        FileObject fr = UtFile.getFileObject(moduleDef.getPath());
        try {
            String s = fr.getName().getRelativeName(fc.getName());
            if (".".equals(s)) {
                s = "";
            }
            return s;
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    /**
     * Текущий путь относительно корня classpath
     */
    protected String getCurPath(ModuleDef moduleDef) {
        String s = getRelPath(moduleDef);
        String s1 = moduleDef.getPackageRoot().replace('.', '/');
        if (!UtString.empty(s)) {
            s1 = s1 + "/" + s;
        }
        return s1;
    }

    public String getVar(String var) throws Exception {
        if ("module".equals(var)) {
            return moduleDef.getName();
        }

        if (vars != null) {
            if (vars.containsKey(var)) {
                return vars.get(var);
            }
        }

        Matcher m = P_MODULE.matcher(var);
        if (m.find()) {
            String var1 = m.group(1);
            ModuleDef md = moduleDef;
            int a = var1.indexOf(':');
            if (a != -1) {
                String moduleName = var1.substring(a + 1);
                var1 = var1.substring(0, a);
                md = moduleDefResolver.findModuleDef(moduleName);
                if (md == null) {
                    throw new XError("Не найден модуль {0} при обработке переменной {1}", moduleName, var);
                }
            }
            return getMacroVar(md, var1);
        }

        return null;
    }

    protected String getMacroVar(ModuleDef moduleDef, String var) {
        if ("name".equals(var)) {
            return moduleDef.getName();

        } else if ("path".equals(var)) {
            return moduleDef.getPath();

        } else if ("package".equals(var)) {
            return moduleDef.getPackageRoot();

        } else if ("package.cur".equals(var)) {
            return getCurPath(moduleDef).replace('/', '.');

        } else if ("package.rel".equals(var)) {
            return getRelPath(moduleDef).replace('/', '.');

        } else if ("packagepath".equals(var)) {
            return moduleDef.getPackageRoot().replace('.', '/');

        } else if ("packagepath.cur".equals(var)) {
            return getCurPath(moduleDef);

        } else if ("packagepath.rel".equals(var)) {
            return getRelPath(moduleDef);

        } else if ("projectpath".equals(var)) {
            if (!moduleDef.getSourceInfo().isSource()) {
                throw new XError("Модуль {0} без исходников, использование projectpath невозможно", moduleDef.getName());
            }
            return moduleDef.getSourceInfo().getProjectPath();

        }
        return null;
    }

    public boolean execFunc(String funcName, Conf params, Conf context) throws Exception {
        if (funcName.equals(ModuleDefConsts.CONF_FUNC_DEPENDS)) {

            if (getLoader().getRoot() != context) {
                throw new XError("Функция {0} может быть только на верхнем уровне",
                        ModuleDefConsts.CONF_FUNC_DEPENDS);
            }

            String v;
            boolean ok = false;

            v = params.getString("module");
            if (!UtString.empty(v)) {
                config.getDepends().add(v);
                ok = true;
            }

            if (!ok) {
                throw new XError("Функция {0} не имеет параметра module",
                        ModuleDefConsts.CONF_FUNC_DEPENDS);
            }

            return true;

        } else {
            return false;
        }

    }

}
