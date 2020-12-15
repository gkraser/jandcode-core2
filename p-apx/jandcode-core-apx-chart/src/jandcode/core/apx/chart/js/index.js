import {echarts, apx} from './vendor'
import * as components from './components'
import langRU from './langRU'
import {hideAllTooltip} from './chart-holder'

export * from './chart-builder'
//

// настройки echarts по умолчанию
apx.jsaBase.cfg.setDefault({
    echarts: {
        // тема echarts по умолчанию
        theme: 'default'
    }
})

// компоненты
apx.jsaVue.registerComponents(components)
// локаль
echarts.registerLocale('RU', langRU)

export {
    components,
    echarts,
    hideAllTooltip,
}