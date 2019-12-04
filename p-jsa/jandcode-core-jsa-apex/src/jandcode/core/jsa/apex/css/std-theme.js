/* Тема std
----------------------------------------------------------------------------- */
import cssQuasar from './std/z-quasar-style.scss'
import css from './std-theme.less'

let config = require('[*]/css/std-config.js')

export default {
    css: [cssQuasar, css],
    config: config
}
