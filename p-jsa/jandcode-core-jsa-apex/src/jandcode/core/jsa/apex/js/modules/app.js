/* Приложение
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

let API_plugin = {

    init: Function,

    beforeRun: Function,

}

/**
 * Приложение.
 * Существует только один его экземпляр - app.
 */
export class App {

    constructor() {
        this.__plugins = []
    }

    /**
     * Подключить плагин приложения.
     * @param plugin плагин
     */
    use(plugin) {
        if (!jsaBase.isObject(plugin)) {
            throw new Error("Плагин приложения должен быть объектом")
        }
        if (this.__plugins.indexOf(plugin) !== -1) {
            return
        }
        this.__plugins.push(plugin)
        if ('init' in plugin) {
            plugin.init(this)
        }
    }

    async run() {
        for (let plugin of this.__plugins) {
            if ('beforeRun' in plugin) {
                await plugin.beforeRun(this)
            }
        }
    }

}

/**
 * Глобальный экземпляр app
 * @type {App}
 */
export let app = new App()

