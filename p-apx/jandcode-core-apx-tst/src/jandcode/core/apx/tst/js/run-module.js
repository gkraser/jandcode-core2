import {apx, jsaBase} from './vendor'

/**
 * Запуск модуля
 * @param moduleName имя модуля.
 *        Должен иметь метод run, или экспортировать vue-компонент с именем 'vue',
 *        или быть vue-модулем (расшрение vue), или описывать тесты mocha.
 */
export async function runModule(moduleName) {

    await Jc.loadModule([moduleName])

    let main = require(moduleName)
    if (main.run) {
        main.run()

    } else if (moduleName.endsWith(".vue") || main.vue) {
        let p = main.vue || main.default || main
        //

        // данные для тестов, загружаются в tstData асинхронна, если есть такая опция
        // в компоненте доступны через this.$attrs.tstData
        let tstData = {}
        if (jsaBase.isFunction(p.tstData)) {
            await p.tstData(tstData)
        }

        if (!jsaBase.app.isRunned()) {
            // приложение не запущено - запускаем свое
            jsaBase.app.run(() => {
                let vm = new apx.Vue({
                    el: '#jc-app',
                    render: h => h(p, {attrs: {tstData: tstData}})
                })
            })
        } else {
            // приложение уже запущено - работаем поверх него
            jsaBase.app.onAfterRun(() => {

                if (p._showFrame) {
                    // если есть _showFrame - просто показываем как фрейм
                    let opts = {frame: p}
                    if (jsaBase.isObject(p._showFrame)) {
                        jsaBase.extend(opts, p._showFrame)
                    }
                    apx.showFrame(opts)
                } else {
                    let vm = new apx.Vue({
                        render: h => h('div', {'class': ['tst-overapp-wrap']}, [h(p, {attrs: {tstData: tstData}})])
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
                }
            })
        }

    } else if (window.mocha) {
        mocha.run()

    } else {
        throw new Error("No run() or mocha.run()")
    }
}

