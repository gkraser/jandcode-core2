/*

Компонентты и утилиты для использования при разработке

----------------------------------------------------------------------------- */

import {Vue} from 'jandcode.core.jsa.vue'

import TstApxPanel from './TstApxPanel'
import TstBtn from './TstBtn'
import TstCheckbox from './TstCheckbox'
import TstSelect from './TstSelect'
import TstFontsize from './TstFontsize'
import TstIconBox from './TstIconBox'
import TstIconList from './TstIconList'
import TstPanels from './TstPanels'

Vue.component(TstApxPanel.name, TstApxPanel)
Vue.component(TstBtn.name, TstBtn)
Vue.component(TstCheckbox.name, TstCheckbox)
Vue.component(TstSelect.name, TstSelect)
Vue.component(TstFontsize.name, TstFontsize)
Vue.component(TstIconBox.name, TstIconBox)
Vue.component(TstIconList.name, TstIconList)
Vue.component(TstPanels.name, TstPanels)

export {
    TstApxPanel,
    TstBtn,
    TstCheckbox,
    TstSelect,
    TstFontsize,
    TstIconBox,
    TstIconList,
    TstPanels,
}


