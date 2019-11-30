/* Приложение
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

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
            svc.onCreate()
            svc.onInit()
            svc.onRun()
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
     * Регистрация функции, которая выполнится после onRun всех сервисов.
     * Если приложение уже запущено, функция выполнится немедленно.
     * @param callback {Function} app передается первым параметром
     */
    onRun(callback) {
        if (this.__runned) {
            callback(this)
        } else {
            let svc = new AppService(this)
            svc.onRun = callback
            __registredCallback.push(svc)
        }
    }

    /**
     * Запуск приложения
     * @return {Promise<void>}
     */
    async run() {
        this.__runned = false
        this.__services = []

        //
        for (let svcClass of __registredServices) {
            let svc = new svcClass(this)
            this.__services.push(svc)
        }

        //
        for (let svc of this.__services) {
            svc.onCreate(this)
        }
        for (let cb of __registredCallback) {
            cb.onCreate(this)
        }

        //
        for (let svc of this.__services) {
            svc.onInit(this)
        }
        for (let cb of __registredCallback) {
            cb.onInit(this)
        }

        this.__runned = true

        //
        for (let svc of this.__services) {
            await svc.onRun(this)
        }
        for (let cb of __registredCallback) {
            await cb.onRun(this)
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
     * Создание сервиса.
     */
    onCreate() {}

    /**
     * Инициализаця сервиса.
     * В момент вызова уже все сервисы созданы и для каждого уже был вызван onCreate
     */
    onInit() {}

    /**
     * Запуск сервиса.
     */
    onRun() {}

}

/**
 * Глобальный экземпляр app
 * @type {App}
 */
export let app = new App()

