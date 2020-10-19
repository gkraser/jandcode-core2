/* Тема apex-base
----------------------------------------------------------------------------- */
import cssQuasar from './apex-base/z-quasar-style.scss'
import css from './apex-base-theme.less'

let config = require('[*]/css/apex-base-config.js')

export default {
    css: [cssQuasar, css],
    config: config
}
