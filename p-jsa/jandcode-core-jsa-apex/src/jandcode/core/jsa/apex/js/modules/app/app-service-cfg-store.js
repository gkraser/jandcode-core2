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
    }

    onRun() {
        this.app.cfgStore.load()
    }

}


