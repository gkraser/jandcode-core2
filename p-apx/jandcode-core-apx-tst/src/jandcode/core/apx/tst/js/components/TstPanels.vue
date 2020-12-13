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
                <component :is="panelComp" :title="titleForNum(n)" class="col"
                           :key="uniKey()" body-fit>
                    <slot name="default">
                        <div>No content for slot default in tst-panels!</div>
                    </slot>
                </component>
            </template>
        </div>

    </div>
</template>

<script>
import * as mixins from '../mixins'
import {apx} from '../vendor'

export default {
    name: 'tst-panels',
    mixins: [mixins.cfgStore],
    components: {},
    props: {
        cfgKey: {
            default: 'panels'
        },

        title: {
            default: 'Панель'
        },

        panelComp: {
            default: 'jc-panel'
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
            let bodyStyle = {
                height: cfg.height
            }
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
        },

        /**
         * Заголовок для панели номер n
         */
        titleForNum(n) {
            if (this.panels.length <= 1) {
                return this.title
            }
            return this.title + ' (' + n + ')'
        }

    },
}
</script>
