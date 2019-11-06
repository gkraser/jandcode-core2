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
    'app.logo': 'img:jandcode/core/apex/jsa/images/icons/app-logo.svg',

    //
    'mail': 'mail',
    'inbox': 'inbox',
}

///
registerIcons(icons)
