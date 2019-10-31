//
import {Vue, VueRouter, Quasar} from './vendor'

export * from 'jandcode.core.jsa.base'

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

export {
    Vue,
    VueRouter,
    Quasar,
}

