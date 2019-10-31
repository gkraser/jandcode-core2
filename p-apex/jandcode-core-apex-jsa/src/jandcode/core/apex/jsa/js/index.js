//
import './vendor'

export * from 'jandcode.core.jsa.base'
export * from 'jandcode.core.jsa.vue'
export * from 'jandcode.core.jsa.quasar'

// quasar icons
let qIconsCss = require('@quasar/extras/material-icons/material-icons.css')
Jc.requireCss(qIconsCss)

// quasar lang
let qLangRu = require('quasar/dist/lang/ru.umd.min.js')
Quasar.lang.set(qLangRu)

