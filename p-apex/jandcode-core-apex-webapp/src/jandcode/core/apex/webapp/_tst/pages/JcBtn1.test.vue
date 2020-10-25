<template>
    <tst-apex-panel class="jcbtn1-test-0bd95535">

        <div class="q-gutter-y-sm">
            <div class="row q-gutter-x-sm">
                <template v-for="c in colors">
                    <jc-btn :label="c" :color="c"/>
                </template>
            </div>

            <template
                    v-for="c in colors">
                <div class="row items-center q-gutter-x-sm" style="border1:1px solid red">
                    <jc-btn :color="c" label="Кнопка"/>
                    <jc-btn :color="c" label="Кнопка<a>" type="a"/>
                    <jc-btn :color="c" label="С иконкой" icon="bus"/>
                    <jc-btn :color="c" label="С иконкой" icon="mail"/>
                    <jc-btn :color="c" icon="mail"/>
                    <jc-btn :color="c" icon="bus"/>
                    <jc-btn :color="c" icon="menu" round/>
                    <!--                    <q-input :value="'Для масштаба:'+(c?c:'norm')" square outlined/>-->
                    <jc-btn :color="c" label="С иконкой справа" icon-right="bus"/>
                    <jc-btn :color="c" label="С иконками" icon="mail" icon-right="bus"/>
                    <div>С иконками (просто текст для сравнения)</div>
                </div>
            </template>
        </div>

    </tst-apex-panel>
</template>

<script>
import {config as btnConfig} from 'jandcode.core.apex.webapp/js/components/btn/JcBtn'


let btnColors = []
for (let z in btnConfig.color) {
    btnColors.push(z)
}

export default {
    props: {},
    data() {
        return {}
    },
    computed: {
        colors() {
            return btnColors
        }
    }
}
</script>

<style lang="less">

.jcbtn1-test-0bd95535 {


}

</style>
