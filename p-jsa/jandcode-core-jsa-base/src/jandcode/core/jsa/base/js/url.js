import cfg from './cfg';
import {jQuery} from "./vendor"

// для формирования абсолютных url
let _tmp_TAG_A = document.createElement('a');

/**
 * Разбор строки параметров в виде объекта
 * @param str строка вида 'a=1&b=2'. Если начинается с '#' или '?', то '#' или '?' игнорируется
 * @return {{}}
 */
export function deparams(str) {
    let obj = {}
    if (str && str !== '#' && str !== '?') {
        if (str.startsWith('#')) {
            str = str.substring(1)
        }
        if (str.startsWith('?')) {
            str = str.substring(1)
        }
        str = decodeURIComponent(str);
        let chunks = str.split('&')
        for (let c = 0; c < chunks.length; c++) {
            let [k, v] = chunks[c].split('=', 2)
            if (!v) {
                v = ''
            }
            obj[k] = v;
        }
    }
    return obj;
}

/**
 * Формирует строку параметров для url (jQuery.param)
 * @param params объект с параметрами
 */
export function params(params) {
    return jQuery.param(params);
}

/**
 * Возвращает текущие параметры страницы в виде объекта
 */
export function getPageParams() {
    return deparams(window.location.search)
}

/**
 * Возвращает текущий hash страницы в виде строки.
 * '#' убран. Декодирован.
 */
export function getPageHash() {
    let hash = window.location.hash
    if (!hash) {
        return ''
    }
    const index = hash.indexOf('#')
    if (index < 0) {
        return ''
    }
    hash = decodeURIComponent(hash)
    hash = hash.slice(index + 1)
    return hash
}

/**
 * Абсолютный ли url
 * @param {String} url
 */
export function isAbs(url) {
    if (!url) {
        return false;
    }
    return url.indexOf(':') !== -1 || url.substr(0, 1) === '/';
}

/**
 * Формирование url-адреса для ссылок.
 * Если url абсолютный, возвращается без изменений.
 * Если url относительный, то считается что он принадлежит приложению
 * и возвращает url с префиксом cfg.baseUrl.
 */
export function ref(url) {
    if (!url) {
        return cfg.baseUrl;
    }
    if (isAbs(url)) {
        return url;
    }
    return cfg.baseUrl + url;
}

/**
 * Возвращает абсолютный url
 */
export function refAbs(url) {
    if (isAbs(url)) {
        return url;
    }
    _tmp_TAG_A.href = ref(url);
    return _tmp_TAG_A.href;
}

