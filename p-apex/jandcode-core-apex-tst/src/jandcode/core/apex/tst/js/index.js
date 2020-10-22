/* Поддержка тестирования в tst для apex
----------------------------------------------------------------------------- */

import './vendor'
import 'jandcode.core.jsa.tst/js'
import {runModule} from './run-module'
import './components'

// глобализация

Jc.tst = Jc.tst || {}
Jc.tst.runModule = runModule

//
export * from './run-module'
