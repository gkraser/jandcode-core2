//
// сначала vendor, что бы jsaBase проинициализировалось
import {Vue, VueRouter} from './vendor'
// все конфигурации по умолчанию из всех модулей
import '[*]/js/cfg-default.js'

import * as components from "./comp"
import * as bem from './utils/bem'

export * from './app'

/**
 * Регистрация компонентов
 * @param comps набор компонентов. Каждый компонент, имеющий name будет зарегистрирован
 */
export function registerComponents(comps) {
    for (let key in comps) {
        let comp = comps[key];
        if (comp.name) {
            Vue.component(comp.name, comp)
        }
    }
}

registerComponents(components)

//todo такая регистрация заставляет idea видеть компоненты!
Vue.component(components.JcToolbar.name, components.JcToolbar)
Vue.component(components.JcAction.name, components.JcAction)

// vue

Vue.config.productionTip = false
Vue.config.devtools = false

//
Vue.use(VueRouter)

// bem for vue

Vue.mixin({
    methods: {

        /**
         * см: bem
         * @param elementOrMods
         * @param mods
         */
        cn(elementOrMods, mods) {
            return bem.bem(this.$options.cn_prefix || this.$options.name, elementOrMods, mods)
        }

    }
})

// глобализация
window.Vue = Vue
window.VueRouter = VueRouter

//////

export {
    Vue,
    VueRouter,
    components,
}
