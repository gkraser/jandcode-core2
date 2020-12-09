<template>
    <div>
        <portal to="tools">
            <q-separator vertical/>
            <tst-select v-model="cfgMy.count" :options="countSets"
                        label="count"/>
            <tst-select v-model="cfgMy.height" :options="heightSets"
                        label="height"/>
            <tst-checkbox label="paddingLeft" v-model="cfgMy.paddingLeft"/>
        </portal>

        <div class="row q-gutter-x-md q-mb-md" :style="bodyStyle">
            <template v-for="n in panels">
                <jc-panel :title="'Панель ' + n" class="col"
                          :key="uniKey()">
                    <PanelWrap :height="panelHeight">
                        <slot name="default">
                            <div>No content for slot default in tst-panels!</div>
                        </slot>
                    </PanelWrap>
                </jc-panel>
            </template>
        </div>

    </div>
</template>

<script>
import * as mixins from '../mixins'
import {apx} from '../vendor'

/**
 * Обертка для показываемой панели, ставит высоту заказанную.
 */
let PanelWrap = {
    functional: true,
    render(h, ctx) {
        let data = apx.jsaVue.adaptCtxData(ctx)
        let res = []
        if (ctx.children && ctx.children.length > 0) {
            let item = ctx.children[0]
            let itemData = apx.jsaVue.adaptCtxData(item)
            if (!apx.jsaBase.isObject(itemData.staticStyle)) {
                itemData.staticStyle = {}
            }
            itemData.staticStyle.height = data.attrs.height || '150px'
            res.push(item)
        }
        return res
    }
}

export default {
    name: 'tst-panels',
    mixins: [mixins.cfgStore],
    components: {
        PanelWrap,
    },
    props: {
        cfgKey: {
            default: 'panels'
        }
    },
    created() {
        this.key = 0
        this.cfgStore.applyDefault({
            [this.cfgKey]: {
                count: '1',
                height: '300px',
                paddingLeft: false
            }
        })
    },
    data() {
        return {
            countSets: ['1', '2', '3', '4', '5'],
            panels: [],
            heightSets: ['100px', '200px', '300px', '400px', '500px'],
            panelHeight: '',
            bodyStyle: {},
            paddingLeftValue: '300px',
        }
    },
    computed: {
        cfgMy() {
            return this.cfg[this.cfgKey]
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfgMy
            //
            let count = apx.jsaBase.toInt(cfg.count, 1)
            let panels = []
            for (let i = 1; i <= count; i++) {
                panels.push('' + i)
            }
            this.panels = panels
            this.panelHeight = cfg.height
            //
            let bodyStyle = {}
            if (cfg.paddingLeft) {
                bodyStyle.paddingLeft = this.paddingLeftValue
            }
            this.bodyStyle = bodyStyle
        },

        // уникальный ключ для использования в цикле отрисовки
        // без него не реагирует на изменения высоты
        uniKey() {
            this.key++
            return '' + this.key
        }
    },
}
</script>
