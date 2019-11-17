/* Поддержка тем
----------------------------------------------------------------------------- */

import * as cnv from './cnv'
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
}