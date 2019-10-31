import 'jandcode.core.apex.jsa'
import './comp'

/**
 * Запуск модуля
 * @param moduleName имя модуля. Должен иметь метод run или описывать тесты mocha.
 */
export function runJs(moduleName) {
    Jc.loadModule([moduleName], function() {
        let main = require(moduleName)
        if (main.run) {
            main.run()
        } else if (window.mocha) {
            let elMocha = document.createElement('div')
            elMocha.id = 'mocha'
            document.body.appendChild(elMocha)
            mocha.run()
        } else {
            throw new Error("No run() or mocha.run()")
        }
    })
}

/**
 * Запуск модуля m_main как vue-компонент
 * @param moduleName имя модуля. Должен быть vue-компонентом
 */
export function runVue(moduleName) {
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
