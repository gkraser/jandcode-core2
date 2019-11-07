/* Основной стандартный набор иконок
----------------------------------------------------------------------------- */

import {registerIcons} from '../utils/icons'

import {quasarIcons} from './quasar-iconSet'

registerIcons(quasarIcons)

let icons = {
    'unknown': 'sentiment_very_dissatisfied',

    //
    'menu': 'menu',
    'error': 'error',
    'info': 'info',

    //
    'caret-down': 'arrow_drop_down',
    'caret-up': 'arrow_drop_up',
    'caret-right': 'arrow_right',
    'caret-left': 'arrow_left',

    //
    'app-logo': 'img:jandcode/core/apex/jsa/images/icons/app-logo.svg',

    //
    'mail': 'mail',
    'inbox': 'inbox',
    'user': 'account_box',
}

///
registerIcons(icons)
