<template>
    <tst-apex-panel class="typography2-test-006a96fa">

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
                            {{ loremSm }}
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
                    <div class="text-h3">Текст</div>
                </q-card-section>
                <q-card-section>
                    {{ lorem }}
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
                        <p>{{ lorem }}</p>
                    </template>
                </q-card-section>
            </q-card>
        </div>

    </tst-apex-panel>
</template>

<script>
import ClassExam from './_components/ClassExam'

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

export default {
    extends: Vue.component('tst-apex-page'),
    components: {
        ClassExam,
    },
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {
            loremEn: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
            loremRu: "Повседневная практика показывает, что постоянный количественный рост и сфера нашей активности требует определения и уточнения новых принципов формирования материально-технической и кадровой базы. Но перспективное планирование предоставляет.",
            textStyles: textStyles,
            textWeights: textWeights,
        }
    },
    computed: {
        lorem() {
            return this.loremRu + " " + this.loremEn;
        },
        loremMd() {
            return this.loremRu.substr(0, 80) + ". " + this.loremEn.substring(0, 80) + ".";
        },
        loremSm() {
            return this.loremRu.substr(0, 50) + ". " + this.loremEn.substring(0, 50) + ".";
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
