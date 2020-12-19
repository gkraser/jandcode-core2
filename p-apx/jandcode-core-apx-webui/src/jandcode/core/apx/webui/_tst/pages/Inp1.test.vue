<template>
    <tst-apx-panel class="inp1-test-c30fa8be">
        <template #tools>
        </template>

        <tst-big-content :maxRows="10"/>

        <div class="row items-center q-gutter-x-md q-mb-lg">
            <jc-inp-str v-model="str1"/>
            <jc-inp-str v-model="str1"/>
            <jc-btn label="change model" @click="str1='332211'"/>
            <div>jc-inp-str</div>
            <jc-btn label="Для масштаба"/>
            <jc-btn label="Для масштаба" icon="bus"/>
            <q-checkbox v-model="bool" label="Метка справа"/>
            <div>model:[{{ str1 }}]</div>
        </div>

        <div class="row items-center q-gutter-x-md q-mb-lg">
            <jc-inp-passwd v-model="passwd1"/>
            <jc-inp-passwd v-model="passwd1"/>
            <jc-btn label="change model" @click="passwd1='777'"/>
            <div>jc-inp-passwd</div>
            <div>model:[{{ passwd1 }}]</div>
        </div>

        <div class="row items-center q-gutter-x-md q-mb-lg">
            <jc-inp-date v-model="date1" id="dd1"/>
            <jc-inp-date v-model="date1" id="dd2"/>
            <jc-btn label="change model" @click="date1='2020-12-21'"/>
            <div>jc-inp-date</div>
            <div>model:[{{ date1 }}]</div>
        </div>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {
            str1: '',
            passwd1: '',
            date1: '1999-12-23',
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

.inp1-test-c30fa8be {


}

</style>
