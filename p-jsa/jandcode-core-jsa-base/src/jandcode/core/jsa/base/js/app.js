/* Приложение
----------------------------------------------------------------------------- */

import * as cnv from './cnv'

let __registredServices = []
let __registredCallback = []

const STAGE_NOT_RUNNED = 0
const STAGE_CREATE = 1
const STAGE_INIT = 2
const STAGE_BEFORE_RUN = 3
const STAGE_RUN = 4
const STAGE_AFTER_RUN = 5
const STAGE_RUNNED = 6

/**
 * Приложение.
 * Существует только один его экземпляр - app.
 */
export class App {

    constructor() {
        this.__runned = false
        this.__services = []
        this.services = {}
        this.__stage = STAGE_NOT_RUNNED
    }

    /**
     * Пересоздание приложения.
     * Используется исключительно в тестах
     */
    static recreateApp() {
        if (app.isRunned()) {
            for (let svc of app.__services) {
                svc.onAfterRun(app)
            }
            for (let cb of __registredCallback) {
                cb.onAfterRun(app)
            }
            app = new App()
        }
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
        if (this.__runned) {
            throw new Error('service нельзя зарегистрировать после запуска приложения')
        }
        if (!serviceClass) {
            throw new Error('serviceClass undefined')
        }
        if (!(serviceClass.prototype instanceof AppService)) {
            throw new Error('serviceClass должен быть классом-наследником от AppService: ' + serviceClass)
        }
        __registredServices.push(serviceClass)
    }

    /**
     * Регистрация функции, которая выполнится после onCreate всех сервисов.
     * Вызывать нужно до запуска приложения.
     * @param callback {Function} app передается первым параметром
     */
    onCreate(callback) {
        if (this.__stage >= STAGE_CREATE) {
            throw new Error("onCreate after run")
        }
        let svc = new AppService(this)
        svc.onCreate = callback
        __registredCallback.push(svc)
    }

    /**
     * Регистрация функции, которая выполнится после onInit всех сервисов.
     * Вызывать нужно до запуска фазы init приложения.
     * @param callback {Function} app передается первым параметром
     */
    onInit(callback) {
        if (this.__stage >= STAGE_INIT) {
            throw new Error("onInit after run init")
        }
        let svc = new AppService(this)
        svc.onInit = callback
        __registredCallback.push(svc)
    }

    /**
     * Регистрация функции, которая выполнится после onBeforeRun всех сервисов.
     * Вызывать нужно до запуска фазы beforeRun приложения.
     * @param callback {Function} app передается первым параметром
     */
    onBeforeRun(callback) {
        if (this.__stage >= STAGE_BEFORE_RUN) {
            throw new Error("onBeforeRun after run beforeRun")
        }
        let svc = new AppService(this)
        svc.onBeforeRun = callback
        __registredCallback.push(svc)
    }

    /**
     * Регистрация функции, которая выполнится после запуска приложения и onAfterRun всех сервисов.
     * Если приложение уже запущено, функция выполнится немедленно.
     * @param callback {Function} app передается первым параметром
     */
    onAfterRun(callback) {
        if (this.__stage >= STAGE_RUNNED) {
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
        this.__stage = STAGE_CREATE
        for (let svc of this.__services) {
            await svc.onCreate(this)
        }
        for (let cb of __registredCallback) {
            await cb.onCreate(this)
        }

        //
        this.__stage = STAGE_INIT
        for (let svc of this.__services) {
            await svc.onInit(this)
        }
        for (let cb of __registredCallback) {
            await cb.onInit(this)
        }

        //
        this.__stage = STAGE_BEFORE_RUN
        for (let svc of this.__services) {
            await svc.onBeforeRun(this)
        }
        for (let cb of __registredCallback) {
            await cb.onBeforeRun(this)
        }

        // собственно запуск
        this.__stage = STAGE_RUN
        await cb(this)

        //
        this.__stage = STAGE_AFTER_RUN
        for (let svc of this.__services) {
            await svc.onAfterRun(this)
        }
        for (let cb of __registredCallback) {
            await cb.onAfterRun(this)
        }

        //
        this.__stage = STAGE_RUNNED

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
     * В момент вызова все экземпляры сервисов уже созданы.
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

    /**
     * Вызывается в момент остановки приложения.
     * Необходимо подчистить за собой следы.
     * Остановка приложения в нормальной работе не делается.
     * Это исключительно для тестов, что бы можно было несколько раз вызывать run.
     */
    onStop() {}

}

/**
 * Глобальный экземпляр app
 * @type {App}
 */
export let app = new App()

