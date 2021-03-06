package jandcode.core.jsa.jsmodule.std;

import jandcode.core.*;
import jandcode.core.jsa.cfg.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.jsmodule.impl.*;

import java.util.*;

/**
 * Скрипт с модулями.
 * Включаются все модули с id из incIds (включая зависимые)
 * и исключая excIds (включая зависимые).
 */
public class ModuleTextModules extends ModuleTextCustom {

    private List<String> incIds;
    private List<String> excIds;

    public ModuleTextModules(App app, List<String> incIds, List<String> excIds) {
        setApp(app);
        this.incIds = incIds;
        this.excIds = excIds;
    }

    protected String makeText() {
        JsaCfg jsaCfg = getApp().bean(JsaCfg.class);
        boolean bodyAsString = jsaCfg.isModuleSource();

        JsModuleService svc = getApp().bean(JsModuleService.class);

        ModuleJoiner joinerInc = new ModuleJoiner(svc);
        ModuleJoiner joinerExc = new ModuleJoiner(svc);

        for (String id : this.incIds) {
            joinerInc.addModule(svc.getModuleById(id));
        }
        for (String id : this.excIds) {
            joinerExc.addModule(svc.getModuleById(id));
        }

        Set<JsModule> modules = new LinkedHashSet<>(joinerInc.getAll());
        modules.removeAll(joinerExc.getAll());

        ModuleScriptBuilder mbs = new ModuleScriptBuilder();

        StringBuilder sb = new StringBuilder();

        for (JsModule mod : modules) {

            mbs.build(sb, mod, bodyAsString);
            sb.append("\n");
        }

        for (String id : this.incIds) {
            sb.append("Jc.require('");
            sb.append(id);
            sb.append("');\n");
        }

        return sb.toString();
    }

}
