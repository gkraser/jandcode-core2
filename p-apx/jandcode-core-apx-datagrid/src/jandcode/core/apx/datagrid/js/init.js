import css from 'tabulator-tables/dist/css/tabulator_simple.min.css'
import {jsaVue} from './vendor'
import * as components from './components'

//todo пока сразу ставим, потом сделать видимо ленивым
Jc.requireCss(css, "before-theme")

// компоненты
jsaVue.registerComponents(components)
