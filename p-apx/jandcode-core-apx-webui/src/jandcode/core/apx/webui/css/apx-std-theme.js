/* Тема apx-std
----------------------------------------------------------------------------- */
import cssQuasar from 'quasar/dist/quasar.css'
import css from './apx-std-theme.less'
import robotoFont from './fonts/font-roboto'

let config = require('[*]/css/apx-std-config.js')

export default {
    css: [].concat(robotoFont, cssQuasar, css),
    config: config
}
