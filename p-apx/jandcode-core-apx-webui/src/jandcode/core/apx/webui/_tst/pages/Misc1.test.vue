<template>
    <tst-apx-panel class="misc1-test-d234d1c0">
        <template #tools>
            <tst-checkbox label="showTooltip" v-model="cfg.showTooltip"/>
            <tst-fontsize/>
        </template>


        <q-card class="q-mb-md">
            <q-card-section>
                <div class="row q-gutter-md items-center">
                    <q-btn label="Кнопка"/>
                    <q-btn label="Кнопка" icon="bus"/>
                    <q-btn label="Кнопка" icon="bus" icon-right="caret-down"/>
                    <q-btn label="Кнопка" icon="bus" icon-right="mail"/>
                    <q-btn label="Кнопка" color="primary"/>

                    <q-btn round color="primary" icon="bus"/>
                    <q-btn round color="amber" glossy text-color="black" icon="mail"/>

                    <q-btn label="Кнопка с tooltip" tooltip="Это такой tooltip">
                        <q-tooltip v-model="cfg.showTooltip">
                            Это такой tooltip
                        </q-tooltip>
                    </q-btn>
                    <div>Просто текст: {{ lorem(100) }}</div>
                </div>
                <div class="q-pa-md q-gutter-sm">
                    <q-btn flat color="primary" label="Flat"/>
                    <q-btn flat rounded color="primary" label="Flat Rounded"/>
                    <q-btn flat round color="primary" icon="mail"/>

                    <q-btn outline color="primary" label="Outline"/>
                    <q-btn outline rounded color="primary" label="Outline Rounded"/>
                    <q-btn outline round color="primary" icon="mail"/>

                    <q-btn push color="primary" label="Push"/>
                    <q-btn push color="primary" round icon="mail"/>
                    <q-btn push label="Push"/>
                    <q-btn push round icon="mail"/>

                    <q-btn unelevated label="Unelevated"/>
                    <q-btn unelevated color="primary" label="Unelevated"/>
                    <q-btn unelevated rounded color="primary" label="Unelevated Rounded"/>
                    <q-btn unelevated round color="primary" icon="mail"/>

                    <q-btn class="glossy" color="teal" label="Glossy"/>
                    <q-btn class="glossy" rounded color="deep-orange"
                           label="Glossy Rounded"/>
                    <q-btn class="glossy" round color="primary" icon="mail"/>
                </div>
            </q-card-section>
        </q-card>

        <q-card>
            <q-card-section>
                <div class="q-pa-md q-gutter-sm">
                    <q-btn v-for="size in sizes"
                           color="primary" :size="size" :label="`Size ${size}`"/>

                    <q-btn v-for="size in sizes"
                           round color="primary" :size="size" icon="mail"/>
                    <label>q-btn</label>
                    <br>
                    <jc-btn v-for="size in sizes"
                            color="primary" :size="size" :label="`Size ${size}`"/>

                    <jc-btn v-for="size in sizes"
                            round color="primary" :size="size" icon="mail"/>
                    <label>jc-btn</label>
                    <br>
                    <jc-btn v-for="size in sizes"
                            color="primary" :size="size" :label="`Size ${size}`"
                            icon="mail"/>
                    <label>jc-btn with icon</label>

                    <br>
                </div>
            </q-card-section>
        </q-card>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore, tst.mixins.lorem],
    components: {},
    created() {
        this.cfgStore.applyDefault({
            showTooltip: false,
        })
    },
    data() {
        return {
            sizes: ['xs', 'sm', 'md', 'lg', 'xl', 'large-6', 'small-3'],
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
    }
}
</script>

<style lang="less">

.misc1-test-d234d1c0 {


}

</style>
