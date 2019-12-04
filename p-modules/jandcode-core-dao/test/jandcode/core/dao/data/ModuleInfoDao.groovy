package jandcode.core.dao.data

import jandcode.core.*
import jandcode.core.dao.*

class ModuleInfoDao extends BaseDao {

    List<ModuleInfo> getModules() {
        println "THIS APP:${getApp()}"

        App app = getContext().getApp()

        List<ModuleInfo> res = new ArrayList<>()
        for (m in app.getModules()) {
            ModuleInfo mi = new ModuleInfo(m)
            res.add(mi)
        }
        return res
    }

    ModuleInfo getModule(String name) {
        App app = getContext().getApp()

        Module m = app.getModules().get(name)
        return new ModuleInfo(m)
    }

}
