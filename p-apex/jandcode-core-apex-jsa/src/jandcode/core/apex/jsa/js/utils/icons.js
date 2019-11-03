/*

Иконки

----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

let _icons = {}

/**
 * По абстрактному имени иконки возвращает то,что хочет видеть quasar.
 * @param name
 * @return {{}}
 */
export function getQuasarIconName(name) {
    if (!name) {
        return name
    }
    if (name.startsWith('img:')) {
        return 'img:' + jsaBase.url.ref(name.substring(4))
    }
    let ic = _icons[name]
    if (ic) {
        if (ic.indexOf(':') !== -1) {
            return getQuasarIconName(ic)
        } else {
            return ic
        }
    }
    return name
}

/**
 * Регистрация иконки
 * @param icons Объект, в котором ключ - имя иконки, значение - иконка
 */
export function registerIcons(icons) {
    jsaBase.extend(_icons, icons)
}

/**
 * Все зарегистрированные иконки
 */
export function getIcons() {
    return _icons
}



