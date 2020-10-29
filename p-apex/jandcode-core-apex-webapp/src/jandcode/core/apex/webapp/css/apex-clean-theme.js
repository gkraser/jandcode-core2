/* Тема apex-clean
----------------------------------------------------------------------------- */
import cssQuasar from 'quasar/dist/quasar.css'
import css from './apex-clean-theme.less'
import robotoFont from './fonts/font-roboto'

let config = {}

export default {
    css: [].concat(robotoFont, cssQuasar, css),
    config: config
}
