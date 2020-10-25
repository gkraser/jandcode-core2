/* Тема apex-std
----------------------------------------------------------------------------- */
import cssQuasar from './apex-std/z-quasar-style.scss'
import css from './apex-std-theme.less'

import f400 from "fontsource-roboto/400.css";
import f100 from "fontsource-roboto/100.css";
import f400c from "fontsource-roboto-condensed/400.css";
import f300c from "fontsource-roboto-condensed/300.css";

let config = require('[*]/css/apex-std-config.js')

export default {
    css: [f400, f100, f400c, f300c, cssQuasar, css],
    config: config
}
