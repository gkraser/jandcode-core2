<template>
    <tst-apex-panel class="typography2-test-006a96fa">
        <template #tools>
            <tst-fontsize/>
        </template>

        <div class="row q-gutter-md q-mb-md">

            <q-card class="col">
                <q-card-section>
                    <div class="text-h3">Заголовки</div>
                </q-card-section>
                <q-card-section>
                    <template v-for="a in 6">
                        <ClassExam :cls="'text-h'+a">
                            Heading {{ a }}
                        </ClassExam>
                    </template>
                </q-card-section>
            </q-card>

            <q-card class="col">
                <q-card-section>
                    <div class="text-h3">Стили</div>
                </q-card-section>
                <q-card-section>
                    <template v-for="a in textStyles">
                        <ClassExam :cls="a">
                            {{ loremMd }}
                        </ClassExam>
                    </template>
                </q-card-section>
            </q-card>


        </div>

        <div class="row q-gutter-md q-mb-md">

            <q-card class="col">
                <q-card-section>
                    <div class="text-h3">Вес шрифта</div>
                </q-card-section>
                <q-card-section>
                    <template v-for="a in textWeights">
                        <ClassExam :cls="a">
                            {{ loremSm }}
                        </ClassExam>
                    </template>
                </q-card-section>
            </q-card>

            <q-card class="col">
                <q-card-section>
                    <div class="text-h3">Размеры</div>
                </q-card-section>
                <q-card-section>
                    <template v-for="a in textSizes">
                        <ClassExam :cls="'text-size-'+a">
                            {{ loremMd }}
                        </ClassExam>
                    </template>
                </q-card-section>
            </q-card>
        </div>

        <div class="row q-gutter-md q-mb-md">


            <q-card class="col">
                <q-card-section>
                    <div class="text-h3">Текст без параграфа</div>
                </q-card-section>
                <q-card-section>
                    {{ loremLg }}
                </q-card-section>
            </q-card>


        </div>


        <div class="row q-gutter-md">
            <q-card class="col" v-for="a in 2">
                <q-card-section>
                    <div class="text-h3">Текст-{{ a }}</div>
                </q-card-section>
                <q-card-section>
                    <template v-for="b in 6">
                        <div :class="'text-h'+b">Заголовок {{ b }}</div>
                        <p>{{ loremLg }}</p>
                    </template>
                </q-card-section>
            </q-card>
        </div>

    </tst-apex-panel>
</template>

<script>
import ClassExam from './_components/ClassExam'
import * as tst from 'jandcode.core.apex.tst'

let textStyles = [
    '__no_style__',
    'text-body1',
    'text-body2',
    'text-caption',
    'text-overline',
    'text-subtitle1',
    'text-subtitle2',
]

let textWeights = [
    'text-weight-thin',
    'text-weight-light',
    'text-weight-regular',
    'text-weight-medium',
    'text-weight-bold',
    'text-weight-bolder',
]

let textSizes = [
    'norm', 'xs', 'sm', 'md', 'lg', 'xl', 'large-1', 'large-2', 'large-3', 'large-4', 'large-5',
    'large-6', 'large-7', 'small-1', 'small-2', 'small-3'
]

export default {
    extends: Vue.component('tst-apex-page'),
    mixins: [tst.mixins.lorem],
    components: {
        ClassExam,
    },
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {
            textStyles: textStyles,
            textWeights: textWeights,
            textSizes: textSizes,
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

.typography2-test-006a96fa {


}

</style>
