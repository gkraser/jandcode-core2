import {apex, jsaBase} from './vendor'

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
            if (!jsaBase.app.isRunned()) {
                // приложение не запущено - запускаем свое
                jsaBase.app.run(() => {
                    let vm = new apex.Vue({
                        el: '#jc-app',
                        render: h => h(p)
                    })
                })
            } else {
                // приложение уже запущено - работаем поверх него
                jsaBase.app.onAfterRun(() => {
                    let vm = new apex.Vue({
                        render: h => h('div', {'class': ['tst-overapp-wrap']}, [h(p)])
                    })
                    vm.$mount()
                    // ищем место для монтирования
                    let pagePlace = document.querySelector(main.mountTo || '.jc-app--main')
                    if (pagePlace) {
                        // нашли место
                        pagePlace.insertAdjacentElement('afterbegin', vm.$el)
                    } else {
                        // не нашли место - во весь экран
                        vm.$el.classList.add('tst-overapp-fullscreen')
                        document.body.appendChild(vm.$el)
                    }
                })
            }

        } else if (window.mocha) {
            mocha.run()

        } else {
            throw new Error("No run() or mocha.run()")
        }
    })
}

