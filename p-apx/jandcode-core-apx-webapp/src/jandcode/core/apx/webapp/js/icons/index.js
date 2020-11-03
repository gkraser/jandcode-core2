/* Основной стандартный набор иконок
----------------------------------------------------------------------------- */

import {registerIcons, quasar_iconMapFn} from '../utils/icons'
import {quasarIcons, quasarIconSet} from './quasar-iconSet'
import svgIcons from './svgicons.js'
import std from './std'
import {Vue} from '../vendor'

/**
 * Инициализация стандартного набора иконок
 */
export function init() {
    registerIcons(quasarIcons)
    registerIcons(svgIcons)
    registerIcons(std)

    // меняем iconSet на свой
    Vue.prototype.$q.iconSet = quasarIconSet

    // iconMap для Quasar
    Vue.prototype.$q.iconMapFn = function(iconName) {
        return quasar_iconMapFn(iconName)
    }
}


