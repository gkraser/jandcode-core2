/* Некоторые настройки приложения по умолчанию
----------------------------------------------------------------------------- */

import {jsaBase} from '../../vendor'

import {createCfgStore, CfgStore} from '../cfg-store'


export class CfgStoreService extends jsaBase.AppService {

    onCreate() {
        let cfgStore = createCfgStore('apx.webapp')

        /**
         * Глобальный CfgStore
         * @member {CfgStore} App#cfgStore
         */
        this.app.cfgStore = cfgStore
    }

    onBeforeRun() {
        this.app.cfgStore.load()
    }

}


