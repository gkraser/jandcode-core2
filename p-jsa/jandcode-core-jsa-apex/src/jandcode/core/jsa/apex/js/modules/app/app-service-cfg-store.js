/* Некоторые настройки приложения по умолчанию
----------------------------------------------------------------------------- */

import {AppService} from '../app'

import {Vue} from '../vue'
import {createCfgStore, CfgStore} from '../cfg-store'

/**
 * Глобальный CfgStore
 * @member {CfgStore} App#cfgStore
 */

export class CfgStoreService extends AppService {

    onCreate() {
        let cfgStore = createCfgStore('apex.app')
        this.app.cfgStore = cfgStore
        Vue.prototype.$jc.cfgStore = cfgStore
    }

    onRun() {
        console.info("onRun CfgStoreService");
        this.app.cfgStore.load()
        this.app.cfgStore.autoSave = true   //todo autoSave должно приложение ставить, если ему это нужно
    }

}


