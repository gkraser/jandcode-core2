/* Инициализация
----------------------------------------------------------------------------- */

//
import {Quasar} from './vendor'
import * as icons from './icons'

// quasar lang
let qLangRu = require('quasar/dist/lang/ru.umd.min.js')
Quasar.lang.set(qLangRu)

// иконки
icons.init()
