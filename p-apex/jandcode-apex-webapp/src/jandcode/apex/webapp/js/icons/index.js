/* Основной стандартный набор иконок
----------------------------------------------------------------------------- */

import {registerIcons, quasar_iconMapFn} from '../utils/icons'
import {quasarIcons, quasarIconSet} from './quasar-iconSet'
import svgIconsAll from './svgicons-all.js'
import std from './std'
import {Vue} from '../vendor'

/**
 * Инициализация стандартного набора иконок
 */
export function init() {
    registerIcons(quasarIcons)
    registerIcons(svgIconsAll)
    registerIcons(std)

    // меняем iconSet на свой
    Vue.prototype.$q.iconSet = quasarIconSet

    // iconMap для Quasar
    Vue.prototype.$q.iconMapFn = function(iconName) {
        return quasar_iconMapFn(iconName)
    }
}


