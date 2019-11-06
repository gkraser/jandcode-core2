//
import {Vue, VueRouter, Quasar, jsaBase} from './vendor'
import * as utils from './utils'

export * from 'jandcode.core.jsa.base'

// иконки
import './icons/std'
import {quasarIconSet} from './icons/quasar-iconSet'

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
    return utils.icons.quasar_iconMapFn(iconName)
}

// меняем iconSet на свой
Vue.prototype.$q.iconSet = quasarIconSet


// fix
require('./fix/fix-iconMapFn')


export {
    Vue,
    VueRouter,
    Quasar,
    utils,
}

