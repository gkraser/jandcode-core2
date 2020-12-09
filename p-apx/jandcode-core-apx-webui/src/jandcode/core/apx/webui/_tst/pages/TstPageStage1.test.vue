<template>
    <tst-apx-panel class="tstpagestage1-test-11d8bfa7">
        <template #tools>
            <tst-checkbox v-model="cfg.param2" label="Param2"/>
            <tst-checkbox v-model="cfg.param2" label="Param2"/>
            <tst-checkbox v-model="cfg.param2" label="Param2"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
        </template>

        <template #tools-2>
            <tst-checkbox v-model="cfg.param2" label="Param2"/>
            <tst-checkbox v-model="cfg.param2" label="Param2"/>
            <tst-checkbox v-model="cfg.param2" label="Param2"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
            <tst-select v-model="cfg.param3" label="Param3" :options="['z1','z2','z3']"/>
            <tst-select v-model="cfg.param4" label="Param3"
                        :options="[{value:'z1', text:'ZZZZ1'},{value:'z2', text:'ZZZZ2'},]"/>
        </template>


        hello111
        <br>
        txt:{{ txt }}

        <br>
        <q-checkbox v-model="cfg.param2" label="Param2"/>

        <q-input v-model="cfg.param3" label="Param3"/>
        <q-input v-model="cfg.param4" label="Param3"/>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({
            param1: 'PAP1VAL',
            param2: true,
            param3: 'z2',
            param4: 'z1',
        })
    },
    data() {
        return {
            txt: 'TXTVAL'
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
            //
            this.txt = this.txt + ':' + cfg.param1 + "-" + cfg.param2
        },
    }
}
</script>

<style lang="less">

.tstpagestage1-test-11d8bfa7 {


}

</style>
