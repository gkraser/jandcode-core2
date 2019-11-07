/*

cfg

Глобальная конфигурация

----------------------------------------------------------------------------- */

import * as base from './base'
import * as cnv from './cnv'


function copyCfgObject(ob) {
    let tmp = Object.assign({}, ob)
    escapeCfgProps(tmp)
    return tmp
}

function escapeCfgProps(ob) {
    delete ob['set']
    delete ob['setDefault']
}

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
        base.extend(true, this, copyCfgObject(cfg))
    }

    /**
     * Установить свойства конфигурации по умолчанию с помощью объекта.
     * Осуществляется глубокое копирование.
     * Если свойства уже есть, они не перезаписываются.
     * @param cfg {Object}
     */
    setDefault(cfg) {
        if (!cfg) {
            return;
        }
        if (!cnv.isObject(cfg)) {
            throw new Error('Cfg.set: need object')
        }
        let tmp = copyCfgObject(cfg)
        base.extend(true, tmp, this)
        escapeCfgProps(tmp)
        base.extend(true, this, tmp)
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

if (Jc.__cfg__) {
    // есть конфигурация по умолчанию
    cfg.set(Jc.__cfg__)
    delete Jc.__cfg__
}

export default cfg;

export {
    Cfg
}
