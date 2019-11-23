import * as apex from 'jandcode.core.jsa.apex'
import './comp'

/**
 * Запуск модуля
 * @param moduleName имя модуля.
 *        Должен иметь метод run, или экспортировать vue-компонент с именем 'vue',
 *        или быть vue-модулем (расшрение vue), или описывать тесты mocha.
 */
export function runModule(moduleName) {
    Jc.loadModule([moduleName]).then(function() {
        let main = require(moduleName)
        if (main.run) {
            main.run()

        } else if (moduleName.endsWith(".vue") || main.vue) {
            let p = main.vue || main.default || main
            //
            apex.app.run().then(() => {
                let vm = new Vue({
                    el: '#jc-app',
                    render: h => h(p)
                })
            })

        } else if (window.mocha) {
            mocha.run()

        } else {
            throw new Error("No run() or mocha.run()")
        }
    })
}

