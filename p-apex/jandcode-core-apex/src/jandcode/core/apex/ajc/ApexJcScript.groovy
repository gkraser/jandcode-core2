package jandcode.core.apex.ajc

import jandcode.core.*
import jandcode.jc.*

/**
 * Базовый класс для скриптов apex
 */
abstract class ApexJcScript extends ProjectScript {

    /**
     * Ссылка на загруженное приложение
     */
    App getApp() {
        return include(ApexAppManager).getApp()
    }

}
