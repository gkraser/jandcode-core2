package jandcode.jsa.jsmodule.std;

import jandcode.jsa.jsmodule.*;
import jandcode.jsa.jsmodule.impl.*;
import jandcode.core.*;

import java.util.*;

/**
 * Скрипт с модулями.
 * Включаются только модули с id из ids, зависимые не трогаются.
 */
public class ModuleTextOnlyModules extends ModuleTextCustom {

    private List<String> ids;

    public ModuleTextOnlyModules(App app, List<String> ids) {
        setApp(app);
        this.ids = ids;
    }

    protected String makeText() {
        JsModuleService svc = getApp().bean(JsModuleService.class);

        ModuleScriptBuilder mbs = new ModuleScriptBuilder();

        StringBuilder sb = new StringBuilder();

        for (String id : this.ids) {
            JsModule mod = svc.getModuleById(id);

            mbs.build(sb, mod, true);
            sb.append("\n");
        }

        return sb.toString();
    }

}
