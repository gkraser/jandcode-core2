/* Инициализация
----------------------------------------------------------------------------- */

//
import {Quasar, jsaVue} from './vendor'
import * as icons from './icons'
import * as components from './components'

// quasar lang
let qLangRu = require('quasar/dist/lang/ru.umd.min.js')
Quasar.lang.set(qLangRu)

// иконки
icons.init()

// компоненты
jsaVue.registerComponents(components)
