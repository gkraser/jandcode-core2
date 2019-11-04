/*

Иконки

----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

const ICON_PREFIX = 'jc:'
const ICON_EMPTY = 'empty'

let _icons = {
    'empty': ' ',  // для material icon - это как бы пустая иконка
}

function fixIconUrl(name) {
    if (name.startsWith('img:')) {
        return 'img:' + jsaBase.url.ref(name.substring(4))
    } else {
        return name
    }
}

/**
 * По имени иконки возвращает то, что хочет видеть quasar.
 * @param name
 * Для 'img:URL' - относительные url преобразуются в вариант с baseUrl.
 * Для 'jc:NAME' - namе ищется в зарегистрированных иконках.
 * Для пустого параметра - возвращается пустая иконка.
 * Для остальных - без изменений.
 * @return {{}}
 */
export function getQuasarIconName(name) {
    if (!name || !jsaBase.isString(name)) {
        return _icons[ICON_EMPTY]
    }
    if (name.startsWith(ICON_PREFIX)) {
        let jcname = name.substring(ICON_PREFIX.length)
        let ic = _icons[jcname]
        if (ic) {
            return ic
        } else {
            if (Jc.cfg.debug) {
                console.warn("Not registren icon: ", name);
            }
            return _icons[ICON_EMPTY]
        }
    }
    return fixIconUrl(name)
}

/**
 * Регистрация иконки
 * @param icons Объект, в котором ключ - имя иконки, значение - иконка
 */
export function registerIcons(icons) {
    if (!icons) {
        return
    }
    for (let nm in icons) {
        _icons[nm] = fixIconUrl(icons[nm])
    }
}

/**
 * Все зарегистрированные иконки
 */
export function getIcons() {
    return _icons
}



