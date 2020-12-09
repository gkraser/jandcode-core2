/* Глобальный CfgStore для тестов
----------------------------------------------------------------------------- */
import {apx} from './vendor'

let _cfgStore

/**
 * Глобальный экземпляр cfgStore для тестов
 * @return {CfgStore}
 */
export function getCfgStore() {
    if (_cfgStore == null) {
        _cfgStore = apx.createCfgStore(Jc.cfg.tst.path)
        _cfgStore.autoSave = true
        _cfgStore.load()
    }
    return _cfgStore
}