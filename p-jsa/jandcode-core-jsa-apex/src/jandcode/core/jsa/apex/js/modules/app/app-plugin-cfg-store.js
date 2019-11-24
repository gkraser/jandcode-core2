/* Некоторые настройки приложения по умолчанию
----------------------------------------------------------------------------- */

import {Vue} from '../vue'
import {createCfgStore, CfgStore} from '../cfg-store'

/**
 * Глобальный CfgStore
 * @member {CfgStore} App#cfgStore
 */

export default {

    init(app) {
        let cfgStore = createCfgStore('apex.app')
        app.cfgStore = cfgStore
        Vue.prototype.$jc.cfgStore = cfgStore
    },

    beforeRun(app) {
        app.cfgStore.load()
        app.cfgStore.autoSave = true
    }

}



