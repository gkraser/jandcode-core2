//
import {Vue, VueRouter, Quasar, jsaBase} from './vendor'
import * as utils from './utils'

export * from 'jandcode.core.jsa.base'

// иконки
import './icons/std'

// vue
Vue.config.productionTip = false
Vue.config.devtools = false

//
Vue.use(VueRouter)

// quasar icons
let qIconsCss = require('@quasar/extras/material-icons/material-icons.css')
Jc.requireCss(qIconsCss)

// quasar lang
let qLangRu = require('quasar/dist/lang/ru.umd.min.js')
Quasar.lang.set(qLangRu)

// глобализация
window.Vue = Vue
window.VueRouter = VueRouter
window.Quasar = Quasar

// утилиты для экземпляров vue
let $jc = {

    /**
     * Преобразование url в baseUrl
     */
    url(u) {
        return jsaBase.url.ref(u)
    },

}
Vue.prototype.$jc = $jc

/**
 * Иконка по имени
 */
Vue.prototype.$q.iconMapFn = function(iconName) {
    let a = utils.icons.fixIconUrl(iconName)
    if (a) {
        return {
            icon: a
        }
    }
    a = utils.icons.getIcon(iconName)
    if (a) {
        return {
            icon: a
        }
    }
}

// fix
require('./fix/fix-iconMapFn')


export {
    Vue,
    VueRouter,
    Quasar,
    utils,
}

