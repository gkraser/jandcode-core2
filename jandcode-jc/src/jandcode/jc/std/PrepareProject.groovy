package jandcode.jc.std


import jandcode.jc.*

/**
 * Подготовка проекта к использованию.
 * Генерирует информацию, которая является жизненно важной для проекта,
 * однако не должна присутствовать в системе контроля версий.
 */
class PrepareProject extends ProjectScript {

    boolean executed

    /**
     * Событие возникает при выполнении команды prepare
     */
    public static class Event_Prepare extends BaseJcEvent {
    }

    protected void onInclude() throws Exception {
        cm.add("prepare", "Подготовка проекта к использованию", this.&cmPrepare)
    }

    void cmPrepare(CmArgs args) {
        // команда выполняется только один раз
        if (executed) {
            return
        }
        executed = true

        log.info "Prepare: ${ut.nameInfo(project)}"

        // сначала генерируем событие
        fireEvent(new Event_Prepare())

        // потом для всех заинтересованных вызываем prepareProject
        for (IPrepareProject pp : impl(IPrepareProject)) {
            pp.prepareProject()
        }

    }

}
