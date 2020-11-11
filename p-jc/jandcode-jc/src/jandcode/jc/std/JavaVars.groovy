package jandcode.jc.std

import jandcode.jc.*

/**
 * Набор переменных, связанный с java-проектами
 */
class JavaVars extends ProjectScript {

    private static groupId_key = JavaVars.class.name + "-groupId"
    private static targetLevel_key = JavaVars.class.name + "-targetLevel"
    private static sourceLevel_key = JavaVars.class.name + "-sourceLevel"

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

    /**
     * Версия java, в которую компилировать.
     * Если не установлена - возвращается пустая строка
     */
    String getTargetLevel() {
        ut.getVar(targetLevel_key, "")
    }

    void setTargetLevel(String v) {
        ut.setVar(targetLevel_key, v)
    }

    /**
     * Версия java, в которой подразумеваются исходники.
     * Если не установлена - возвращается пустая строка
     */
    String getSourceLevel() {
        ut.getVar(sourceLevel_key, "")
    }

    void setSourceLevel(String v) {
        ut.setVar(sourceLevel_key, v)
    }

}
