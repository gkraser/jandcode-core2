/*

cfg

Глобальная конфигурация

----------------------------------------------------------------------------- */

import * as base from './base'
import * as cnv from './cnv'


/**
 * Конфигурация
 */
class Cfg {

    /**
     * Установить свойства конфигурации с помощью объекта.
     * Осуществляется глубокое копирование.
     * @param cfg {Object}
     */
    set(cfg) {
        if (!cfg) {
            return;
        }
        if (!cnv.isObject(cfg)) {
            throw new Error('Cfg.set: need object')
        }
        base.extend(true, this, cfg)
    }

}

/**
 * Глобальный экземпляр конфигурации
 */
let cfg = new Cfg()

// базовая конфигурация по умолчанию
cfg.set({

    /**
     * Базовый url для добавления в начало загружаемых url. Должен заканчиваться '/'
     */
    baseUrl: '/',

    /**
     * Среда выполнения debug/release
     */
    debug: false,

})


export default cfg;

export {
    Cfg
}
