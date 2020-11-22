/* Обработка ошибок
----------------------------------------------------------------------------- */

import {Vue, Quasar, jsaBase} from '../../vendor'

function defaultShowErrorUi(error) {
    error = jsaBase.createError(error)
    setTimeout(function() {
        // внутри timeout, а то иногда warn приводит к зацикливанию
        Quasar.Notify.create({
            position: 'top-right',
            multiLine: true,
            color: 'negative',
            message: '' + error.message,
            icon: 'error',
            actions: [
                {label: 'Закрыть', color: 'yellow'},
            ]
        })
    }, 50)
}

export let showErrorUi = defaultShowErrorUi

export class ErrorHandlersService extends jsaBase.AppService {

    onBeforeRun() {
        let th = this

        function vueError(err, vm, info, ew) {
            let error = jsaBase.createError(err)

            error.vm = vm
            error.info = `Apx: [Vue ${ew}] ${info}`

            th.showError(error)
            console.log('[ERROR] ' + error.info + ": " + error.message)
            console.log('vm', vm)
            th.showStack(err)
        }

        Vue.config.errorHandler = function(err, vm, info) {
            vueError(err, vm, info, 'error')
        }

        Vue.config.warnHandler = function(err, vm, info) {
            vueError(err, vm, info, 'warn')
        }

        this._onErrorOld = window.onerror
        this._onErrorNew = function(message, url, line, col, error) {
            let msg = `[Fatal error]: ${message} [${line}:${col}] ${url}`
            if (th.ignoreError(msg)) {
                return
            }
            console.error(msg)
            th.showError(message)
        }
        window.onerror = this._onErrorNew

        this._unhandledrejection = function(event) {
            let error = jsaBase.createError(event.reason)

            console.log('[ERROR promise] ', error.message)
            console.log(event)
            th.showError(error)
            console.error(event.reason)
            event.preventDefault();
        }
        window.addEventListener("unhandledrejection", this._unhandledrejection);

    }

    onStop() {
        if (this._onErrorNew) {
            window.onerror = this._onErrorOld
        }
        if (this._unhandledrejection) {
            window.removeEventListener("unhandledrejection", this._unhandledrejection);
        }
    }

//////

    ignoreError(message) {
        if (!message) {
            return false
        }
        if (message.indexOf('ResizeObserver loop limit exceeded') !== -1) {
            return true
        }
        return false
    }

    showStack(err) {
        console.error(err);
    }

    showError(err) {
        showErrorUi(err)
    }

}


