<template>
    <tst-apx-panel class="datagrid1-test-69e5515a">
        <template #tools>
            <tst-btn label="dialog1" @click="dialog1"/>
        </template>

        <div style="row">
            <jc-datagrid :options="opt1" style="height:400px"/>
        </div>

    </tst-apx-panel>
</template>

<script>
import {apx, tst} from '../vendor'
import * as grids from './grids'
import Dialog1 from './Dialog1'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
        //
        this.opt1 = grids.grid1({})
    },
    data() {
        return {}
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
        dialog1() {
            apx.showDialog({
                frame: Dialog1
            })
        }
    }
}
</script>

<style lang="less">

.datagrid1-test-69e5515a {


}

</style>
