<template>
    <tst-apx-panel class="jcbtn1-test-0bd95535">
        <template #tools>
            <tst-fontsize/>
        </template>


        <div class="q-gutter-y-sm">
            <div>kind:</div>
            <div class="row q-gutter-x-sm">
                <template v-for="c in kinds">
                    <jc-btn :label="c" :kind="c"/>
                </template>
            </div>

            <template
                    v-for="c in kinds">
                <div class="row items-center q-gutter-x-sm" style="border1:1px solid red">
                    <jc-btn :kind="c" label="Кнопка"/>
                    <jc-btn :kind="c" label="Кнопка<a>" type="a"/>
                    <jc-btn :kind="c" label="С иконкой" icon="bus"/>
                    <jc-btn :kind="c" label="С иконкой" icon="mail"/>
                    <jc-btn :kind="c" icon="mail"/>
                    <jc-btn :kind="c" icon="bus"/>
                    <!--                    <q-input :value="'Для масштаба:'+(c?c:'norm')" square outlined/>-->
                    <jc-btn :kind="c" label="С иконкой справа" icon-right="bus"/>
                    <jc-btn :kind="c" label="С иконками" icon="mail" icon-right="bus"/>
                    <div>С иконками (просто текст для сравнения)</div>
                </div>
            </template>
        </div>

    </tst-apx-panel>
</template>

<script>
import {config as btnConfig} from 'jandcode.core.apx.webapp/js/components/btn/JcBtn'


let btnKinds = []
for (let z in btnConfig.kind) {
    btnKinds.push(z)
}

export default {
    extends: Vue.component('tst-apx-page'),

    props: {},
    data() {
        return {}
    },
    computed: {
        kinds() {
            return btnKinds
        }
    }
}
</script>

<style lang="less">

.jcbtn1-test-0bd95535 {


}

</style>
