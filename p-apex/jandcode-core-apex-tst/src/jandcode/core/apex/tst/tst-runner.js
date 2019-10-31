import 'jandcode.core.apex.jsa'
import './comp'

/**
 * Запуск модуля
 * @param moduleName имя модуля.
 *        Должен иметь метод run, быть vue-модулем или описывать тесты mocha.
 */
export function runModule(moduleName) {
    Jc.loadModule([moduleName], function() {
        let main = require(moduleName)
        if (main.run) {
            main.run()

        } else if (moduleName.endsWith(".vue")) {
            let p = main.default || main
            //
            let vm = new Vue({
                el: '#jc-app',
                render: h => h(p)
            })

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

