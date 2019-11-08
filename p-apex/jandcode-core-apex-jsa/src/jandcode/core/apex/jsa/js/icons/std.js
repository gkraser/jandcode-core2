/* Основной стандартный набор иконок
----------------------------------------------------------------------------- */

import {registerIcons} from '../utils/icons'
import {registerSvgIcons} from '../utils/svgicons'

import {quasarIcons} from './quasar-iconSet'
import svgIconsAll from './svgicons-all.js'

// quasar icons
registerIcons(quasarIcons)

// svg icons
registerSvgIcons(svgIconsAll)
let svgicons = {}
for (let nm in svgIconsAll) {
    svgicons[nm] = 'svg:' + nm
}
registerIcons(svgicons)

// явно зарегистрированные
let icons = {
    'unknown': 'sentiment_very_dissatisfied',

    //
    'menu': 'menu',
    'error': 'error',
    'info': 'info',
    'cancel': 'cancel',

    'ins': 'add',
    'del': 'remove',
    'upd': 'edit',

    //
    'caret-down': 'arrow_drop_down',
    'caret-up': 'arrow_drop_up',
    'caret-right': 'arrow_right',
    'caret-left': 'arrow_left',

    //
    'mail': 'mail',
    'inbox': 'inbox',
    'user': 'account_box',
    'admin': 'supervisor_account',
    'calendar': 'calendar_today',
    'clear': 'clear',
    'find': 'search',
    'home': 'home',

}

///
registerIcons(icons)
