/*

Иконки

----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import {getSvgIconId} from './svgicons'

const ICON_EMPTY = 'empty'
const ICON_UNKNOWN = 'unknown'

let _icons = {
    [ICON_EMPTY]: ' ',    // для material icon - это как бы пустая иконка
    [ICON_UNKNOWN]: ' ',  // для material icon - это как бы неизвестная иконка
}

let _holderUnregistredIcons = {}

// для svg иконок
let vmDummy = new Vue({})
let h = vmDummy.$createElement

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

/**
 *Реализация quasar.iconMapFn по умолчанию
 */
export function quasar_iconMapFn(iconName) {
    let a = fixIconUrl(iconName)
    if (a) {
        return {
            icon: a
        }
    }

    if (iconName.startsWith(' ')) {
        if (iconName.trim() === '') {
            return {
                icon: getIcon(ICON_EMPTY)
            }
        }
    }

    a = getIcon(iconName)
    if (a) {
        if (a.startsWith('svg:')) {
            // svg иконка
            let b = a.substring(4)
            b = getSvgIconId(b)
            if (b) {
                return {
                    cls: 'jc-svgicon',
                    content: h('svg', [h('use', {attrs: {"href": '#' + b}})])
                }
            } else {
                // не зарегистрирована svg
                if (!_holderUnregistredIcons[a]) {
                    console.warn('Unregistred svg icon:', a, 'for', iconName)
                    _holderUnregistredIcons[a] = 1
                }
            }
        } else {
            return {
                icon: a
            }
        }
    }

    if (Jc.cfg.debug) {
        // уведомление в отладочном режиме и только раз
        if (!_holderUnregistredIcons[iconName]) {
            console.warn('Unregistred icon:', iconName)
            _holderUnregistredIcons[iconName] = 1
        }
    }

    // возвращаем как неизвестную
    return {
        icon: getIcon(ICON_UNKNOWN)
    }
}



