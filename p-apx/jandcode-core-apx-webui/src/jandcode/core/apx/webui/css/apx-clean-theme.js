/* Тема apx-clean
----------------------------------------------------------------------------- */
import cssQuasar from 'quasar/dist/quasar.css'
import css from './apx-clean-theme.less'
import robotoFont from './fonts/font-roboto'

let config = {}

export default {
    css: [].concat(robotoFont, cssQuasar, css),
    config: config
}
