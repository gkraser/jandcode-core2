/*

    Конфигурация с автоматической записью в localStore

 */

import {Vue} from './vue'
import {jsaBase} from '../vendor'
import lodashGet from 'lodash/get'
import lodashSet from 'lodash/set'


function deepCopy(to, from, override, createProp) {
    for (let name in from) {
        let value = from[name]
        if (!(name in to)) {
            if (createProp) {
                if (jsaBase.isObject(value)) {
                    let newTo = Vue.set(to, name, {})
                    deepCopy(newTo, value, override, createProp)
                } else {
                    Vue.set(to, name, value)
                }
            }
        } else {
            let curValue = to[name]
            if (jsaBase.isObject(curValue) && jsaBase.isObject(value)) {
                deepCopy(curValue, value, override, createProp)
            } else if (!jsaBase.isObject(curValue) && !jsaBase.isObject(value)) {
                if (override) {
                    to[name] = value
                }
            } else {
                throw new Error("Не совместимость типов свойств " + name)
            }
        }
    }
}

export let CfgStore = {

    render() {},

    created() {
        this.__cfgDefault = {}
        this.__cfgLoaded = {}
        this.configKey = __filename
        this.autoSave = true
        this.$watch('cfg', this.onChangeCfg, {
            deep: true
        })
    },

    data() {
        return {

            /**
             * Конфигурация
             * @member {Object} CfgStore#cfg
             */
            cfg: {}
        }
    },

    methods: {

        applyDefault(obj) {
            deepCopy(this.__cfgDefault, obj, true, true)
            deepCopy(this.cfg, obj, false, true)
            deepCopy(this.cfg, this.__cfgLoaded, true, false)
        },

        reset(path) {
            if (!path) {
                this.__cfgLoaded = {}
                deepCopy(this.cfg, this.__cfgDefault, true, true)
            } else {
                let subDefault = lodashGet(this.__cfgDefault, path)
                if (subDefault === undefined) {
                    return // нет значения по умолчанию, путь не правильный?
                }
                if (!jsaBase.isObject(subDefault)) {
                    return // это не объект
                }
                let subCfg = lodashGet(this.cfg, path)
                if (subCfg === undefined) {
                    return // нет значения, что то не то...
                }
                if (!jsaBase.isObject(subCfg)) {
                    return // это не объект
                }
                lodashSet(this.__cfgLoaded, path, {})
                deepCopy(subCfg, subDefault, true, true)
            }
        },

        onChangeCfg(newVal, oldVal) {
            if (this.autoSave) {
                this.save()
            }
        },

        save() {
            deepCopy(this.__cfgLoaded, this.cfg, true, true)
            window.localStorage.setItem(this.configKey, JSON.stringify(this.__cfgLoaded))
        },

        load() {
            let s = window.localStorage.getItem(this.configKey)
            if (s) {
                try {
                    this.__cfgLoaded = JSON.parse(s)
                    if (!jsaBase.isObject(this.__cfgLoaded)) {
                        this.__cfgLoaded = {}
                    }
                } catch(e) {
                    this.__cfgLoaded = {}
                }
            } else {
                this.__cfgLoaded = {}
            }
            deepCopy(this.cfg, this.__cfgLoaded, true, false)
        },
    }
}

/**
 * @return {CfgStore}
 */
export function createCfgStore(configKey) {
    let Constr = Vue.extend(CfgStore)
    let inst = new Constr()
    if (configKey) {
        inst.configKey = configKey
    }
    return inst
}


