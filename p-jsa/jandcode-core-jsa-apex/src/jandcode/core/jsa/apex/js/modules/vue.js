/* Инициализация vue
----------------------------------------------------------------------------- */

import {Vue, VueRouter, jsaBase} from '../vendor'
import {app, App} from './app'

// vue
Vue.config.productionTip = false
Vue.config.devtools = false

//
Vue.use(VueRouter)

// глобализация
window.Vue = Vue
window.VueRouter = VueRouter

// утилиты для экземпляров vue
let $jc = {

    cfg: Jc.cfg,

    /**
     * Формирование url-адреса для ссылок.
     */
    ref(url) {
        return jsaBase.url.ref(url)
    }
}

/**
 * Приложение
 * @member {App} $jc#app
 */

Object.defineProperty($jc, 'app', {
    get: function() {
        return app
    }
})


Vue.prototype.$jc = $jc


export {
    Vue,
    VueRouter,
}
