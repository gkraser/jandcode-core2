package jandcode.core.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Построитель файла для настройки приложения _app.cfx
 *
 * Делаем include(AppCfxBuilder) в обработчике события
 * {@link PrepareProject.Event_Prepare}. Методом appendHandler() добавляем обработчик,
 * который будет модифицировать _app.cfx
 */
class AppCfxBuilder extends ProjectScript implements IPrepareProject {

    private List<AppCfxBuilderHandler> handlers = []

    void prepareProject() {
        if (handlers.size() == 0) {
            return // обработчиков нет
        }

        String appCfxFile = wd("_app.cfx")
        SimXml data = new SimXmlNode()

        if (UtFile.exists(appCfxFile)) {
            try {
                SimXmlLoader ldr = new SimXmlLoader(data)
                ldr.setLoadComment(true)
                ldr.load().fromFile(appCfxFile)
            } catch (e) {
                log.warn("Ошибка при загрузке файла ${appCfxFile}: ${UtError.createErrorInfo(e).text}")
                data.clear()
            }
        }

        // обрабатываем
        for (h in this.handlers) {
            h.updateContent(data)
        }

        log "update _app.cfx: ${appCfxFile}"
        SimXmlSaver svr = new SimXmlSaver(data)
        svr.save().toFile(appCfxFile)
    }

    /**
     * Добавить обработчик для построения _app.cfx
     */
    void appendHandler(AppCfxBuilderHandler h) {
        if (h != null) {
            this.handlers.add(h)
        }
    }

}
