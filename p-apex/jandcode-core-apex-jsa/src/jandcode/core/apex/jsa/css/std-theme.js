/* Тема std
----------------------------------------------------------------------------- */
import css from './std-theme.scss'
import robotoFontCss from '@quasar/extras/roboto-font/roboto-font.css'

let config = require('[*]/css/std-config.js')

export default {
    css: [robotoFontCss, css],
    config: config
}