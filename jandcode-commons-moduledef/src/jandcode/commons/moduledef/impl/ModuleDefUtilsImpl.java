package jandcode.commons.moduledef.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import jandcode.commons.simxml.*;

import java.util.*;

public class ModuleDefUtilsImpl {

    /**
     * Загружает описание модуля из conf
     */
    public static ModuleDef loadModuleDefFromConf(Conf conf) {
        // здесь описан модуль

        String m_name = conf.getName();

        String m_path = conf.getString("path");

        ModuleDef res = UtModuleDef.createModuleDef(m_name, m_path, null, null);

        String m_depends = conf.getString("depends");
        res.getDepends().addAll(UtCnv.toList(m_depends));

        for (Conf p : conf.getConfs("prop")) {
            res.getProps().put(p.getName(), p.getString("value"));
        }

        return res;
    }

    public static void saveModuleDefToXml(ModuleDef m, SimXml x) {
        x.setValue("name", m.getName());
        x.setValue("path", m.getPath());
        x.setValue("depends", UtString.join(m.getDepends(), ","));
        //
        for (String key : m.getProps().keySet()) {
            SimXml x1 = x.addChild("prop");
            x1.setValue("name", key);
            x1.setValue("value", UtCnv.toString(m.getProps().get(key)));
        }
    }

    public static List<ModuleDef> loadModuleDefsFromConfFile(String filename) throws Exception {
        NamedList<ModuleDef> res = new DefaultNamedList<>();
        Conf r = UtConf.create();
        UtConf.load(r).fromFile(filename);
        for (Conf x : r.getConfs("system/module-def")) {
            ModuleDef m = loadModuleDefFromConf(x);
            res.add(m);
        }
        return res;
    }

}