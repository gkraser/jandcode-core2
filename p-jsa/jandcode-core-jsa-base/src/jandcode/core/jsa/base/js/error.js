/*

Обработка ошибок

----------------------------------------------------------------------------- */

let ERROR_AJAX_PREFIX = "ERROR_AJAX:";

import * as cnv from './cnv'

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

    getMessage(devMode) {
        let m = this.getMessageInternal(this.err, devMode)
        if (cnv.isObject(m)) {
            let s
            try {
                s = JSON.stringify(m)
            } catch(e) {
                s = '' + m + ' [error stringify] ' + e
            }
            m = s
        }
        return m
    }

    /**
     * Превращает ошибку в текст сообщения
     */
    getMessageInternal(e, devMode) {
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
                    rr = s.match(/<pre class="error">((\n|\r|.)*?)<\/pre>/);
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
            if (cnv.isObject(e.response)) {
                if (e.response.data) {
                    return this.getMessageInternal(e.response.data, devMode)
                }
            }
            return "" + e.message;

        } else if (cnv.isString(e)) {
            return parseTextError(e)

        } else if ('status' in e && 'statusText' in e) {
            // response
            s = e.responseText;
            if (cnv.isString(s)) {
                return parseTextError(s)
            }

            let z = ''
            if (e.status) {
                z = z + e.status
            }
            if (e.statusText) {
                if (z.length > 0) {
                    z = z + ' '
                }
                z = z + e.statusText
            }

            if (z.length > 0) {
                return z
            }

        } else if (e.message) {
            return e.message
        }

        return e;
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

