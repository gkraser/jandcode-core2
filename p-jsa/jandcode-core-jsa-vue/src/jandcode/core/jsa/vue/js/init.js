/* Инициализация vue
----------------------------------------------------------------------------- */

import {Vue, jsaBase} from './vendor'

// vue
Vue.config.productionTip = false
Vue.config.devtools = false

// глобализация
window.Vue = Vue

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
        return jsaBase.app
    }
})


Vue.prototype.$jc = $jc


export {
    Vue,
}
