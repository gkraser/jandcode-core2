/*

Иконки

----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

const ICON_PREFIX = 'jc:'
let _icons = {}

/**
 * По абстрактному имени иконки возвращает то,что хочет видеть quasar.
 * @param name
 * @return {{}}
 */
export function getQuasarIconName(name) {
    if (!jsaBase.isString(name)) {
        return name
    }
    if (name.startsWith('img:')) {
        return 'img:' + jsaBase.url.ref(name.substring(4))
    }
    if (name.startsWith(ICON_PREFIX)) {
        let jcname = name.substring(ICON_PREFIX.length)
        let ic = _icons[jcname]
        if (ic) {
            if (ic.startsWith('img:')) {
                return 'img:' + jsaBase.url.ref(ic.substring(4))
            } else {
                return ic
            }
        } else {
            if (Jc.cfg.debug) {
                console.warn("Not registren icon: ", name);
            }
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



