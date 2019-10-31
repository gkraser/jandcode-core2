/*

Компонентты и утилиты для использования при разработке

----------------------------------------------------------------------------- */

import TstPanel from './TstPanel'
import TstBigContent from './TstBigContent'
import {Vue} from 'jandcode.core.apex.jsa'

Vue.component(TstPanel.name, TstPanel)
Vue.component(TstBigContent.name, TstBigContent)

export {
    TstPanel,
    TstBigContent,
}

