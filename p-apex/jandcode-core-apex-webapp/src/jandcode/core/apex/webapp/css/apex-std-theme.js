/* Тема apex-std
----------------------------------------------------------------------------- */
import cssQuasar from './apex-std/z-quasar-style.scss'
import css from './apex-std-theme.less'

import f100 from "fontsource-roboto/100.css";
import f300 from "fontsource-roboto/300.css";
import f400 from "fontsource-roboto/400.css";
import f500 from "fontsource-roboto/500.css";
import f700 from "fontsource-roboto/700.css";
import f900 from "fontsource-roboto/900.css";

import f400c from "fontsource-roboto-condensed/400.css";
import f300c from "fontsource-roboto-condensed/300.css";

let config = require('[*]/css/apex-std-config.js')

export default {
    css: [
        f100, f300, f400, f500, f700, f900,
        f400c, f300c,
        cssQuasar, css
    ],
    config: config
}
