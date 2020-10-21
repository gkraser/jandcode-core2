/* Поддержка тестирования в tst для apex
----------------------------------------------------------------------------- */

import 'jandcode.apex.webapp'
import 'jandcode.core.jsa.tst/js'
import {runModule} from './run-module'

// глобализация

Jc.tst = Jc.tst || {}
Jc.tst.runModule = runModule

//
export * from './run-module'
