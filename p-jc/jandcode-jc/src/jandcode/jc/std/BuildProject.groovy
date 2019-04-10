package jandcode.jc.std


import jandcode.jc.*

/**
 * Полная сборка проекта.
 */
class BuildProject extends ProjectScript {

    /**
     * Сборка проекта
     */
    static class Event_Build extends BaseJcEvent {
    }

    boolean executed

    protected void onInclude() throws Exception {
        cm.add("build", this.&cmBuild, "Полная сборка проекта")
    }

    void cmBuild(CmArgs args) {
        // команда выполняется только один раз
        if (executed) {
            return
        }
        executed = true

        log.info "Build: ${ut.nameInfo(project)}"

        // чистим
        if (cm.find("clean")) {
            cm.exec("clean")
        }

        // подготавливаем
        if (cm.find("prepare")) {
            cm.exec("prepare")
        }

        // собирайте что хотите
        fireEvent(new Event_Build())
    }

}
