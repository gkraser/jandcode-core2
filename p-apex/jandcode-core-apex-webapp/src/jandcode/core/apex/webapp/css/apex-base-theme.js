/* Тема apex-base
----------------------------------------------------------------------------- */
import cssQuasar from 'quasar/dist/quasar.css'
import css from './apex-base-theme.less'
import robotoFont from './fonts/font-roboto'

let config = require('[*]/css/apex-base-config.js')

export default {
    css: [].concat(robotoFont, cssQuasar, css),
    config: config
}
