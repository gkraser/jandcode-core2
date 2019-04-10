import {Vue} from 'jandcode.jsa.core'
import './comp'

/**
 * Запуск модуля m_main как vue-компонент
 * @param moduleName имя модуля. Должен быть vue-компонентом
 */
export function runModule(moduleName) {
    Jc.loadModule([moduleName], function() {
        let page = require(moduleName)
        let p = page.default || page
        //
        let vm = new Vue({
            el: '#jc-app',
            render: h => h(p)
        })
    })
}