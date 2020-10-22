/* Тема apex-std
----------------------------------------------------------------------------- */
import cssQuasar from './apex-std/z-quasar-style.scss'
import css from './apex-std-theme.less'

let config = require('[*]/css/apex-std-config.js')

export default {
    css: [cssQuasar, css],
    config: config
}
