/*

Обработка ошибок

----------------------------------------------------------------------------- */

let ERROR_AJAX_PREFIX = "ERROR_AJAX:";

import * as cnv from './cnv'
import cfg from './cfg'

/**
 * Объект "ошибка"
 */
export class JcError {

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

        this.message = this.getMessage()
    }

    /**
     * Превращает ошибку в текст сообщения
     */
    getMessage(devMode) {
        let e = this.err;
        let m = "";
        let s;

        function parseTextError(s) {
            let m = s
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
                        m = s
                    }
                } else {
                    m = s;
                }
            }
            return m
        }


        if (e instanceof Error) {
            return "" + e.message;

        } else if (cnv.isString(e)) {
            return parseTextError(e)

        } else if (e.status && e.statusText) {
            // response
            s = e.responseText;
            if (cnv.isString(s)) {
                return parseTextError(s)
            }
            
        } else if (e.message) {
            return e.message
        }
        return "" + e;
    }

}

/**
 * Создает объект JcError из err, если err не JcError.
 * Возвращает или новый объект JcError или err, если err это JcError
 */
export function createError(err) {
    let e = err;
    if (!(err instanceof JcError)) {
        e = new JcError({err: err});
    }
    return e;
}

