package jandcode.core.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.core.jsa.jsmodule.*;

import java.util.*;

/**
 * Построитель текста модуля
 */
public class ModuleScriptBuilder {

    public String build(JsModule module, boolean bodyAsString) {
        StringBuilder sb = new StringBuilder();
        build(sb, module, bodyAsString);
        return sb.toString();
    }

    public void build(StringBuilder sb, JsModule module, boolean bodyAsString) {

        sb.append("Jc.moduleDef('");
        sb.append(module.getName());
        sb.append("','");
        sb.append(module.getId());
        sb.append("',");

        JsModuleService svc = module.getApp().bean(JsModuleService.class);

        Map<String, Object> reqMap = new LinkedHashMap<>();
        for (RequireItem ri : module.getRequires()) {
            List<String> realNames = new ArrayList<>();
            for (String rn : ri.getRealNames()) {
                JsModule rnMod = svc.findModule(rn);
                if (rnMod == null) {
                    realNames.add(rn);
                } else {
                    realNames.add(rnMod.getId());
                }
            }
            reqMap.put(ri.getUsedName(), realNames);
        }
        sb.append(UtJson.toJson(reqMap));
        sb.append(",");

        if (bodyAsString) {
            sb.append(UtJson.toJson(module.getText()));
        } else {
            sb.append("function(exports, require, module, __filename, __dirname){\n");
            sb.append(module.getText());
            sb.append("\n}");
        }

        sb.append(");");

    }

}
