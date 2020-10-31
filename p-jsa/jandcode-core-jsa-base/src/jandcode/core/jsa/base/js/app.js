/* Приложение
----------------------------------------------------------------------------- */

import * as cnv from './cnv'

let __registredServices = []
let __registredCallback = []

/**
 * Приложение.
 * Существует только один его экземпляр - app.
 */
export class App {

    constructor() {
        this.__runned = false
        this.__services = []
        this.services = {}
    }

    /**
     * true - приложение уже запущено
     * @return {boolean}
     */
    isRunned() {
        return this.__runned
    }

    /**
     * Регистрация сервиса приложения
     * @param serviceClass класс сервиса
     */
    registerService(serviceClass) {
        if (!serviceClass) {
            throw new Error('serviceClass undefined')
        }
        if (!(serviceClass.prototype instanceof AppService)) {
            throw new Error('serviceClass должен быть классом-наследником от AppService: ' + serviceClass)
        }
        __registredServices.push(serviceClass)
        if (this.__runned) {
            let svc = new serviceClass(this)
            this.__services.push(svc)
            let name = svc.getName()
            if (name) {
                this.services[name] = svc
            }
            svc.onCreate()
            svc.onInit()
            svc.onBeforeRun()
            svc.onAfterRun()
        }
    }

    /**
     * Регистрация функции, которая выполнится после onCreate всех сервисов.
     * Если приложение уже запущено, функция выполнится немедленно.
     * @param callback {Function} app передается первым параметром
     */
    onCreate(callback) {
        if (this.__runned) {
            callback(this)
        } else {
            let svc = new AppService(this)
            svc.onCreate = callback
            __registredCallback.push(svc)
        }
    }

    /**
     * Регистрация функции, которая выполнится после onInit всех сервисов.
     * Если приложение уже запущено, функция выполнится немедленно.
     * @param callback {Function} app передается первым параметром
     */
    onInit(callback) {
        if (this.__runned) {
            callback(this)
        } else {
            let svc = new AppService(this)
            svc.onInit = callback
            __registredCallback.push(svc)
        }
    }

    /**
     * Регистрация функции, которая выполнится после onBeforeRun всех сервисов.
     * Если приложение уже запущено, функция выполнится немедленно.
     * @param callback {Function} app передается первым параметром
     */
    onBeforeRun(callback) {
        if (this.__runned) {
            callback(this)
        } else {
            let svc = new AppService(this)
            svc.onBeforeRun = callback
            __registredCallback.push(svc)
        }
    }

    /**
     * Регистрация функции, которая выполнится после запуска приложения и onAfterRun всех сервисов.
     * Если приложение уже запущено, функция выполнится немедленно.
     * @param callback {Function} app передается первым параметром
     */
    onAfterRun(callback) {
        if (this.__runned) {
            callback(this)
        } else {
            let svc = new AppService(this)
            svc.onAfterRun = callback
            __registredCallback.push(svc)
        }
    }

    /**
     * Запуск приложения
     * @param cb функция для запуска приложения. Выполняется после onCreate, onInit, onBeforeRun
     * всех сервисов. После этой функции выполняется onAfterRun всех сервисов.
     * Функция может быть асинхронной.
     */
    async run(cb) {
        if (!cnv.isFunction(cb)) {
            throw new Error("Первый параметр в app.run должен быть функцией")
        }
        if (this.__runned) {
            throw new Error("Приложение уже запущено")
        }
        this.__runned = true
        //
        for (let svcClass of __registredServices) {
            let svc = new svcClass(this)
            this.__services.push(svc)
            let name = svc.getName()
            if (name) {
                this.services[name] = svc
            }
        }

        //
        for (let svc of this.__services) {
            await svc.onCreate(this)
        }
        for (let cb of __registredCallback) {
            await cb.onCreate(this)
        }

        //
        for (let svc of this.__services) {
            await svc.onInit(this)
        }
        for (let cb of __registredCallback) {
            await cb.onInit(this)
        }

        //
        for (let svc of this.__services) {
            await svc.onBeforeRun(this)
        }
        for (let cb of __registredCallback) {
            await cb.onBeforeRun(this)
        }

        // собственно запуск
        await cb(this)

        //
        for (let svc of this.__services) {
            await svc.onAfterRun(this)
        }
        for (let cb of __registredCallback) {
            await cb.onAfterRun(this)
        }

    }

}

/**
 * Сервис приложения
 */
export class AppService {

    constructor(app) {
        this.app = app
    }

    /**
     * Имя сервиса
     * @return {*}
     */
    getName() {
        let name = this.constructor.name
        if (!name) {
            return null
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1)
    }

    /**
     * Создание сервиса.
     */
    onCreate() {}

    /**
     * Инициализаця сервиса.
     * В момент вызова уже все сервисы созданы и для каждого уже был вызван onCreate
     */
    onInit() {}

    /**
     * Запуск сервиса перед запуском приложения.
     * В момент вызова уже все сервисы созданы и для каждого уже был вызван onInit
     */
    onBeforeRun() {}

    /**
     * Запуск сервиса после запуска приложения.
     * В момент вызова приложение уже запущено.
     */
    onAfterRun() {}

}

/**
 * Глобальный экземпляр app
 * @type {App}
 */
export let app = new App()

