/* Некоторые настройки приложения по умолчанию
----------------------------------------------------------------------------- */

import {app} from './app'
import {Vue} from './vue'
import {createCfgStore, CfgStore} from './cfg-store'


/**
 * Экземпляр CfgStore
 * @member {CfgStore} App#cfgStore
 */

app.use(function(app) {
    let cfgStore = createCfgStore(__filename)
    cfgStore.load()
    app.cfgStore = cfgStore
    Vue.prototype.$jc.cfgStore = cfgStore
})



