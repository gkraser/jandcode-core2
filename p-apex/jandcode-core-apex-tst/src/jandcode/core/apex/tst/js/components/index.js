/*

Компонентты и утилиты для использования при разработке

----------------------------------------------------------------------------- */

import {Vue} from 'jandcode.core.jsa.vue'

import TstApexPanel from './TstApexPanel'
import TstApexPage from './TstApexPage'
import TstBtn from './TstBtn'
import TstCheckbox from './TstCheckbox'

Vue.component(TstApexPanel.name, TstApexPanel)
Vue.component(TstApexPage.name, TstApexPage)
Vue.component(TstBtn.name, TstBtn)
Vue.component(TstCheckbox.name, TstCheckbox)

export {
    TstApexPanel,
    TstApexPage,
    TstBtn,
    TstCheckbox,
}


