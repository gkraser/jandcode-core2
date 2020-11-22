/* Тема apx-base
----------------------------------------------------------------------------- */
import cssQuasar from 'quasar/dist/quasar.css'
import css from './apx-base-theme.less'
import robotoFont from './fonts/font-roboto'

let config = require('[*]/**/css/apx-base-config.js')

export default {
    css: [].concat(robotoFont, cssQuasar, css),
    config: config
}
