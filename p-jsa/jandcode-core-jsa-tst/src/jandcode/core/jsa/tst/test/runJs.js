import './comp/index'

/**
 * Запуск модуля
 * @param moduleName имя модуля. Должен иметь метод run или описывать тесты mocha.
 */
export function runModule(moduleName) {
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
