//
// сначала vendor, что бы jsaBase проинициализировалось
import {Vue, VueRouter} from './vendor'
//

// vue
Vue.config.productionTip = false
Vue.config.devtools = false

// глобализация
window.Vue = Vue
window.VueRouter = VueRouter

//
Vue.use(VueRouter)

//////

export {
    Vue,
    VueRouter,
}
