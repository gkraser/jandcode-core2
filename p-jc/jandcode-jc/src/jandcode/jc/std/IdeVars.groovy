package jandcode.jc.std

import jandcode.jc.*

/**
 * Набор переменных для использования в ide.
 * Все переменные наследуются из rootProject.
 */
class IdeVars extends ProjectScript {

    private static moduleGroup_key = IdeVars.class.name + "-moduleGroup"

    ////// moduleGroup

    /**
     * В какую группу входит модуль по умолчанию (для idea).
     */
    String getModuleGroup() {
        ut.getVar(moduleGroup_key, "")
    }

    void setModuleGroup(String v) {
        ut.setVar(moduleGroup_key, v)
    }

}
