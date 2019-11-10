//
import {Vue, VueRouter, Quasar, jsaBase} from './vendor'
import * as utils from './utils'
import * as baseapp from './baseapp'
import * as components from './components'
import './icons/std'
import {quasarIconSet} from './icons/quasar-iconSet'

//
utils.comps.registerComponents(components)

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
$jc.cfg = Jc.cfg

Vue.prototype.$jc = $jc

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

export {
    Vue,
    VueRouter,
    Quasar,
    utils,
    components,
    baseapp,
}



