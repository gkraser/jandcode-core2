import {echarts, apx} from './vendor'
import * as components from './components'
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

export {
    components,
    echarts,
}