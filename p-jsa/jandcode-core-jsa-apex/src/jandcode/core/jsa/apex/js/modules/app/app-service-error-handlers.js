/* Обработка ошибок
----------------------------------------------------------------------------- */

import {Vue, Quasar, jsaBase} from '../../vendor'
import {AppService} from '../app'

export class ErrorHandlersService extends AppService {

    onRun() {
        let th = this

        Vue.config.errorHandler = function(err, vm, info) {
            let msg = `Apex: [Vue error]: ${err} ${info}`
            th.showError(msg)
        }

        Vue.config.warnHandler = function(err, vm, info) {
            let msg = `Apex: [Vue warn]: ${err} ${info}`
            th.showError(msg)
        }

        window.onerror = function(message, url, line, col, error) {
            console.error(`[Fatal error]: ${message} [${line}:${col}] ${url}`)
            th.showError(message)
        }

        window.addEventListener("unhandledrejection", function(event) {
            console.error('[Promise error]:', event.reason)
            th.showError(event.reason)
        });

    }

    //////

    showError(err) {
        let e = jsaBase.createError(err)
        console.error(e);
        setTimeout(function() {
            // внутри timeout, а то иногда warn приводит к зацикливанию
            Quasar.Notify.create({
                position: 'top-right',
                multiLine: true,
                color: 'negative',
                message: '' + e.message,
                icon: 'error',
                actions: [
                    {label: 'Закрыть', color: 'yellow'},
                ]
            })
        }, 50)
    }

}


