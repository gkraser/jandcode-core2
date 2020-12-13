<template>
    <tst-apx-panel class="inpanel1-test-0a01af92">
        <template #tools>
        </template>

        <div class="row q-gutter-x-md q-mb-md" style="height:400px">
            <jc-panel title="chart1" class="col" body-fit>
                <jc-chart :options="chart1"/>
            </jc-panel>
            <jc-panel title="chart1" class="col" body-fit>
                <jc-chart :options="chart1"/>
            </jc-panel>
        </div>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'
import {Chart1} from './Chart1'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
        //
        this.chart1 = new Chart1()
    },
    data() {
        return {}
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
    }
}
</script>

<style lang="less">

.inpanel1-test-0a01af92 {


}

</style>
