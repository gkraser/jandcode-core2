package jandcode.core.dao.data

import jandcode.core.*

class ModuleInfo {

    String name
    String path
    boolean isSrc

    ModuleInfo() {
    }

    ModuleInfo(Module m) {
        this.name = m.getName()
        this.path = m.getPath()
        this.isSrc = m.getSourceInfo().isSource()
    }
}
