/* Обработка ошибок
----------------------------------------------------------------------------- */

import {Vue, Quasar} from '../../vendor'

function showError(err) {
    Quasar.Notify.create({
        position: 'top-right',
        multiLine: true,
        color: 'negative',
        message: '' + err,
        icon: 'error',
        actions: [
            {label: 'Закрыть', color: 'yellow'},
        ]
    })
}

export default {

    init(app) {
    },

    beforeRun(app) {

        Vue.config.errorHandler = function(err, vm, info) {
            console.error(`Apex: [Vue error]: ${err} ${info}`)
            console.error(err);
            showError(err)
        }

        Vue.config.warnHandler = function(err, vm, info) {
            console.error(`Apex: [Vue warn]: ${err} ${info}`)
            console.error(err);
            showError(err)
        }

        window.onerror = function(message, url, line, col, error) {
            console.error(`[Fatal error]: ${message} [${line}:${col}] ${url}`)
            showError(message)
        }

    }

}



