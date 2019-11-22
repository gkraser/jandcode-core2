//
import {Vue, VueRouter} from './modules/vue'
import {Quasar, jsaBase} from './vendor'
import * as utils from './utils'
import * as components from './components'
import './icons/std'
import {quasarIconSet} from './icons/quasar-iconSet'

//
utils.comps.registerComponents(components)

// quasar icons
let qIconsCss = require('@quasar/extras/material-icons/material-icons.css')
Jc.requireCss(qIconsCss)

// quasar lang
let qLangRu = require('quasar/dist/lang/ru.umd.min.js')
Quasar.lang.set(qLangRu)

// глобализация
window.Quasar = Quasar
Jc.apex = exports

/**
 * Иконка по имени
 */
Vue.prototype.$q.iconMapFn = function(iconName) {
    return utils.icons.quasar_iconMapFn(iconName)
}

// меняем iconSet на свой
Vue.prototype.$q.iconSet = quasarIconSet

//////

export * from 'jandcode.core.jsa.base'
export * from './components'
export * from './modules'
export * from './baseapp'

export {
    Vue,
    VueRouter,
    Quasar,
    utils,
    components,
}
