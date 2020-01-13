/* Основной стандартный набор иконок
----------------------------------------------------------------------------- */

import {registerIcons} from '../utils/icons'
import {quasarIcons} from './quasar-iconSet'
import svgIconsAll from './svgicons-all.js'

// quasar icons
registerIcons(quasarIcons)
// svg icons
registerIcons(svgIconsAll)

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
    'caret-down': 'svg:caret-down',
    'caret-up': 'svg:caret-up',
    'caret-right': 'svg:caret-right',
    'caret-left': 'svg:caret-left',

    //
    'mail': 'mail',
    'inbox': 'inbox',
    'user': 'account_box',
    'admin': 'supervisor_account',
    'calendar': 'calendar_today',
    'clear': 'clear',
    'find': 'search',
    'home': 'home',
    'close': 'close',
    'frame': 'web_asset',

}

///
registerIcons(icons)
