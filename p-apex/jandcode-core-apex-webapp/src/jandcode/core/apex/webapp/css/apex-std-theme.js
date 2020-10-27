/* Тема apex-std
----------------------------------------------------------------------------- */
import cssQuasar from './apex-std/z-quasar-style.scss'
import css from './apex-std-theme.less'
import robotoFont from './fonts/font-roboto'

let config = require('[*]/css/apex-std-config.js')

export default {
    css: [].concat(robotoFont, cssQuasar, css),
    config: config
}
