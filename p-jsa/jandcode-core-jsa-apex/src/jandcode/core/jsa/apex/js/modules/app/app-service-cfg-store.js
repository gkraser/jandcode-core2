/* Некоторые настройки приложения по умолчанию
----------------------------------------------------------------------------- */

import {jsaBase} from '../../vendor'

import {createCfgStore, CfgStore} from '../cfg-store'

/**
 * Глобальный CfgStore
 * @member {CfgStore} App#cfgStore
 */

export class CfgStoreService extends jsaBase.AppService {

    onCreate() {
        let cfgStore = createCfgStore('apex.app')
        this.app.cfgStore = cfgStore
    }

    onRun() {
        this.app.cfgStore.load()
    }

}


