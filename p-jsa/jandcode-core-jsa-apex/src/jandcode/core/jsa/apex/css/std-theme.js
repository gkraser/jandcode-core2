/* Тема std
----------------------------------------------------------------------------- */
import cssQuasar from './std/z-quasar-style.scss'
import cssRobotoFont from '@quasar/extras/roboto-font/roboto-font.css'
import css from './std-theme.less'

let config = require('[*]/css/std-config.js')

export default {
    css: [cssRobotoFont, cssQuasar, css],
    config: config
}
