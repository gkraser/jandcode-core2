package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Очистка каталога с проектом от временных файлов
 */
class CleanProject extends ProjectScript {

    /**
     * Очистка проекта
     */
    static class Event_Clean extends BaseJcEvent {
    }

    /**
     * Уведомление перед очисткой проекта.
     * clean чистит каталог temp, но в нем могут быть вещи, которые нужны.
     * Например для JavaProject возможно скрытая компиляция и использование
     * кода в других проектов, тогда их чистить не нужно.
     */
    static class Event_BeforeClean extends BaseJcEvent {
    }

    /**
     * Чистить ли temp.
     *
     * clean чистит каталог temp, но в нем могут быть вещи, которые нужны.
     * Например для JavaProject возможно скрытая компиляция и использование
     * кода в других проектов, тогда их чистить не нужно.
     *
     * Перед выполнением устанавливается в true,
     * в обработчике Event_BeforeClean можно установить false.
     */
    boolean cleanTemp = true

    boolean executed

    protected void onInclude() throws Exception {
        cm.add("clean", this.&cmClean, "Очистка от временных файлов")
    }

    void cmClean(CmArgs args) {
        // команда выполняется только один раз
        if (executed) {
            return
        }
        executed = true

        log.info "Clean: ${ut.nameInfo(project)}"

        // сначала уведомляем о начале
        cleanTemp = true
        fireEvent(new Event_BeforeClean())

        // делаем свою работу
        if (cleanTemp) {
            String temp = wd("temp")
            if (UtFile.exists(temp)) {
                ut.cleandir(temp)
            }
        }

        // чистите кто что хочет
        fireEvent(new Event_Clean())
    }

}
