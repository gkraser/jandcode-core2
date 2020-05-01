package jandcode.jc.std

import jandcode.jc.*

/**
 * Набор переменных, связанный с java-проектами
 */
class JavaVars extends ProjectScript {

    private static groupId_key = JavaVars.class.name + "-groupId"

    /**
     * maven: groupId.
     * Если явно не указан = project.name
     */
    String getGroupId() {
        ut.getVar(groupId_key, project.name)
    }

    void setGroupId(String v) {
        ut.setVar(groupId_key, v)
    }

}
