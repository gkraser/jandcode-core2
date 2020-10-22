/*

Компонентты и утилиты для использования при разработке

----------------------------------------------------------------------------- */

import {Vue} from 'jandcode.core.jsa.vue'

import TstApexPanel from './TstApexPanel'
import TstApexPage from './TstApexPage'
import TstBtn from './TstBtn'
import TstCheckbox from './TstCheckbox'
import TstSelect from './TstSelect'

Vue.component(TstApexPanel.name, TstApexPanel)
Vue.component(TstApexPage.name, TstApexPage)
Vue.component(TstBtn.name, TstBtn)
Vue.component(TstCheckbox.name, TstCheckbox)
Vue.component(TstSelect.name, TstSelect)

export {
    TstApexPanel,
    TstApexPage,
    TstBtn,
    TstCheckbox,
    TstSelect,
}


