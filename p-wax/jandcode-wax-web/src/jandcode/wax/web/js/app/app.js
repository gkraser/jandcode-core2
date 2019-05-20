/* Приложение
----------------------------------------------------------------------------- */

let _appInst
let _appInitHandlers = []

export let ob = {a:1}
export function assignOb(newOb) {
    ob = newOb
}

/**
 * Приложение
 */
export class App {

    /**
     * Регистрация обработчика инициализации экземпляра приложения
     * @param fn функция вида fn(app)
     */
    static onInit(fn) {
        if (fn && _appInitHandlers.indexOf(fn) === -1) {
            _appInitHandlers.push(fn)
        }
    }

    constructor() {
        //

        // инициализация
        for (let handler of _appInitHandlers) {
            handler(this)
        }
    }

    m111(){

    }

}

/**
 * Возвращает глобальный экземпляр App по умолчанию
 * @return {App}
 */
export function getApp() {
    if (!_appInst) {
        _appInst = new App()
    }
    return _appInst
}

