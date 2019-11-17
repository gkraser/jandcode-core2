/* Поддержка тем
----------------------------------------------------------------------------- */

import * as cnv from './cnv'
import * as base from './base'
import cfg from './cfg'

cfg.set({
    theme: {}
})

let API_THEME = {
    css: Object,
    config: Array | Object
}

/**
 * Применить тему
 * @param theme либо объект с конфигурацией тиемы, либо имя модуля,
 *              который экспортирует конфигурацию темы.
 */
export function applyTheme(theme) {
    if (cnv.isString(theme)) {
        theme = require(theme)
    }
    if (theme.default) {
        theme = theme.default
    }
    //
    Jc.requireCss(theme.css, 'theme')

    //
    let config = {}
    if (cnv.isArray(theme.config)) {
        for (let it of theme.config) {
            if (it.default) {
                it = it.default
            }
            base.extend(true, config, it)
        }
    } else if (cnv.isObject(theme.config)) {
        let it = theme.config
        if (it.default) {
            it = it.default
        }
        base.extend(true, config, it)
    }

    // заменяем на новый config
    cfg.__values.theme = config
    // обновляем конфигурацию, что бы наложилсь default
    cfg.set({})
}