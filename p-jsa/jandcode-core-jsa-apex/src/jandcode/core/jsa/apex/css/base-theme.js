/* Тема base
----------------------------------------------------------------------------- */
import cssQuasar from './base/z-quasar-style.scss'
import css from './base-theme.less'

let config = require('[*]/css/base-config.js')

export default {
    css: [cssQuasar, css],
    config: config
}
