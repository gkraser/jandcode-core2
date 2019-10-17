/*

Обработка ошибок

----------------------------------------------------------------------------- */

let ERROR_AJAX_PREFIX = "ERROR_AJAX:";

import * as cnv from './cnv'
import cfg from './cfg'

/**
 * Объект "ошибка"
 */
class JcError {

    constructor(config) {
        /**
         * Оригинальная ошибка
         */
        this.err = null

        /**
         * Признак "обработано"
         */
        this.handled = false

        /**
         * Тип err, например 'json'
         */
        this.type = null

        if (config) {
            Object.assign(this, config)
        }
    }

    /**
     * Превращает ошибку в текст сообщения
     */
    getMessage(devMode) {
        let e = this.err;
        let m = "";
        let s;
        if (e instanceof Error) {
            return "" + e.message;
        } else if (e.status && e.statusText) {
            // response
            s = e.responseText;
            if (s) {
                // есть текст
                let s1 = s.substr(0, ERROR_AJAX_PREFIX.length);
                if (s1 === ERROR_AJAX_PREFIX) {
                    m = s.substring(ERROR_AJAX_PREFIX.length);
                } else {
                    let rr = s.match(/<body.*?>((\n|\r|.)*?)<\/body>/);
                    if (rr) s = rr[1];
                    if (!devMode) {
                        rr = s.match(/<div class="error-text">((\n|\r|.)*?)<\/div>/);
                        if (rr) {
                            m = rr[1];
                        } else {
                            m = "" + e.status + ": " + e.statusText;
                        }
                    } else {
                        m = s;
                    }
                }
                //
            }
            return m;
        } else if (cnv.isString(e)) {
            return e;
        }
        return "" + e;
    }

}

/**
 * Создает объект JcError из err, если err не JcError.
 * Возвращает или новый объект JcError или err, если err это JcError
 */
function errorCreate(err) {
    let e = err;
    if (!(err instanceof JcError)) {
        e = new JcError({err: err});
    }
    return e;
}

/**
 * Метод вызывается в catch для регистрации/показа ошибки.
 * Автоматически отправляет дальше (throw) вариант ошибки JcError
 */
function error(err, doThrow) {
    let e = errorCreate(err);
    if (!e.handled) {
        e.handled = true;
        errorShow(e);
    }
    if (doThrow || doThrow === undefined) {
        throw e;
    }
}

/**
 * Стандартный механизм для показа ошибки
 * @param err ошибка
 */
export function errorShow(err) {
    let e = errorCreate(err)
    let devMode = cfg.debug
    console.error('Error:', e.getMessage(devMode));
}
