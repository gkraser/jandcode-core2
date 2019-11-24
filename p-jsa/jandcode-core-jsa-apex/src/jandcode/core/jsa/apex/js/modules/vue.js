/* Инициализация vue
----------------------------------------------------------------------------- */

import {Vue, VueRouter} from '../vendor'

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
    cfg: Jc.cfg
}

Vue.prototype.$jc = $jc


export {
    Vue,
    VueRouter,
}
