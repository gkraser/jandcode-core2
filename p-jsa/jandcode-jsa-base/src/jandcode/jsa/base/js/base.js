/* Базовые функции и объекты
----------------------------------------------------------------------------- */

import {jQuery} from "./vendor"

// счетчик для nextId
let _nextId_cnt = 0

// префикс по умолчанию для nextId
let _nextId_defaultPrefix = 'jc-'

/**
 * Генерация следующего уникального id.
 * @param prefix префикс id. По умолчанию 'jc-'
 * @return {String} id
 */
export function nextId(prefix) {
    _nextId_cnt++;
    if (!prefix) prefix = _nextId_defaultPrefix;
    return '' + prefix + _nextId_cnt;
}

/**
 * Синоним для jQuery.extend, параметры - такие же.
 */
export function extend(dest, o1) {
    return jQuery.extend.apply(jQuery, arguments);
}

/**
 * Синоним для jQuery.ready
 */
export function ready(callback) {
    jQuery(callback)
}

