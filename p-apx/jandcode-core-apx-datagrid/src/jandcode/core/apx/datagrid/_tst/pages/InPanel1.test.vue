<template>
    <tst-apx-panel class="inpanel1-test-c135e6b6">
        <template #tools>
            <tst-btn label="dialog1" @click="dialog1"/>
        </template>

        <div class="row q-gutter-x-md q-mb-md" style="height:250px">

            <jc-panel class="col" title="Это панель 2" body-fit no-padding>
                <template #toolbar>
                    <jc-action icon="more-v"/>
                    <jc-action icon="bus" label="Привет"/>
                </template>

                <jc-datagrid :options="opt1" style1="width:500px"/>

                <template #footer>
                    Это footer
                </template>
            </jc-panel>

            <jc-panel class="col" title="Грида" body-fit no-padding>
                <template #toolbar>
                    <jc-action icon="more-v"/>
                    <jc-action icon="bus" label="Привет"/>
                </template>
                <jc-datagrid :options="opt2" style1="width:500px"/>
<!--                <template #footer>-->
<!--                    Это footer-->
<!--                </template>-->
            </jc-panel>

            <jc-panel class="col-4" body-fit no-padding>
                <jc-datagrid :options="opt2" style1="width:500px"/>
            </jc-panel>

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
        this.opt1 = grids.grid1({})
        this.opt2 = grids.grid1({})
        this.opt3 = grids.grid1({})
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

.inpanel1-test-c135e6b6 {


}

</style>
