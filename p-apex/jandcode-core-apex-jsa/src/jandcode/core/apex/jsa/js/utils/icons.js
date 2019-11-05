/*

Иконки

----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

const ICON_EMPTY = 'empty'

let _icons = {
    'empty': ' ',  // для material icon - это как бы пустая иконка
}

/**
 * Для иконки вида 'img:URL' превращает url в абсолютный
 * и возвращает полученную иконку 'img:NEWURL'.
 * Для остальных - возвращает undefined
 */
export function fixIconUrl(name) {
    if (name.startsWith('img:')) {
        return 'img:' + jsaBase.url.ref(name.substring(4))
    }
}

/**
 * По имени зарегистрированной иконки возвращает то, зарегистрированно.
 * Если ненайдено - возвращает undefined
 */
export function getIcon(name) {
    let ic = _icons[name]
    if (ic) {
        return ic
    }
}

/**
 * Регистрация иконок
 * @param icons Объект, в котором ключ - имя иконки, значение - иконка
 */
export function registerIcons(icons) {
    if (!icons) {
        return
    }
    for (let nm in icons) {
        let v = icons[nm]
        let a = fixIconUrl(v)
        if (a) {
            _icons[nm] = a
        } else {
            _icons[nm] = v
        }
    }
}

/**
 * Все зарегистрированные иконки
 */
export function getIcons() {
    return _icons
}



