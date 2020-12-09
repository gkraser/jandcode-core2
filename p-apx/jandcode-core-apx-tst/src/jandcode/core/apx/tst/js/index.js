/* Поддержка тестирования в tst для apx
----------------------------------------------------------------------------- */

import './vendor'
import 'jandcode.core.jsa.tst/js'
import {runModule} from './run-module'
import * as mixins from './mixins'
import * as components from './components'
import cssTst from './css/tst.css'

Jc.requireCss(cssTst)

// глобализация

Jc.tst = Jc.tst || {}
Jc.tst.runModule = runModule

//
export * from './run-module'
export * from './cfg-store'

//
export {
    mixins,
    components,
}