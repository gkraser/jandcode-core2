/* Утилиты для модулей
----------------------------------------------------------------------------- */

/**
 * Возвращает функцию-фабрику для ленивой загрузки компонента.
 * Как требует router
 */
export function lazyComponent(moduleName) {
    return function() {
        return new Promise(function(resolve) {
            Jc.loadModule(moduleName).then(function() {
                let m = require(moduleName)
                resolve(m)
            })
        })
    }
}

