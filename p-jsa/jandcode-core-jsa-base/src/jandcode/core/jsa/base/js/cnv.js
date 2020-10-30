/* Конверторы значений
----------------------------------------------------------------------------- */

import {jQuery} from "./vendor"

/**
 * Проверка на функцию
 */
export function isFunction(a) {
    return jQuery.isFunction(a);
}

/**
 * Проверка на массив
 */
export function isArray(a) {
    return Array.isArray(a);
}

/**
 * Проверка на строку
 */
export function isString(a) {
    return typeof a === 'string';
}

/**
 * Проверка на простой объект '{}'
 */
export function isObject(a) {
    return jQuery.isPlainObject(a);
}

/**
 * Проверка на boolean
 */
export function isBoolean(a) {
    return typeof a === 'boolean';
}

/**
 * Проверка на number
 */
export function isNumber(a) {
    return typeof a === 'number' && isFinite(a);
}

/**
 * Проверка на date
 */
export function isDate(a) {
    return a instanceof Date;
}

/**
 * Проверка, что a - это числовой символ
 */
export function isNumChar(a) {
    return isString(a) && a.length === 1 && a >= '0' && a <= '9';
}

/**
 * Проверка, что a - начинается с числа
 */
export function isStartNumChar(a) {
    return isString(a) && isNumChar(a.charAt(0));
}

/**
 * Проверка на пустое значение: null, undefined, 0, '', {}, []
 * @param a
 */
export function isEmpty(a) {
    let b = !!a;
    if (!b) {
        return true
    }
    if (isArray(a)) {
        return a.length === 0
    }
    if (isObject(a)) {
        return jQuery.isEmptyObject(a)
    }
    return false
}

//////

/**
 * Перевод строки в число
 * @param s        строка. Если строка начинается с цифры, то она будет сконверирована
 *                 по правилам parseInt, т.е.: toInt('4px')===4
 * @param defValue значение по умолчанию, если преобразование не удалось (по умолчанию 0)
 */
export function toInt(s, defValue) {
    let v
    if (isNumber(s)) return s;
    if (!defValue) defValue = 0;
    try {
        v = parseInt(s);
    } catch(e) {
        v = defValue;
    }
    if (isNaN(v)) v = defValue;
    return v;
}

/**
 * Перевод в boolean
 * @param s        строка. Если строка начинается с цифры, то она будет сконверирована
 *                 по правилам parseInt, т.е.: toInt('4px')===4
 * @param defValue значение по умолчанию, если преобразование не удалось (по умолчанию 0)
 */
export function toBoolean(s, defValue) {
    let v
    if (isBoolean(s)) return s;
    if (!defValue) defValue = false;
    if (s == null) {
        return defValue
    }
    if (s === 1 || s === '1' || s === 'true' || s === 'yes') {
        return true
    }
    return false
}


