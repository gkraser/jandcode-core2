<template>
    <tst-apx-panel class="getoptions1-test-164005da">
        <template #tools>
        </template>

        <tst-panels>
            <jc-chart :options="chart1"></jc-chart>
        </tst-panels>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

class MyChart1 {
    getOptions() {
        console.info("getOptions", this);
        return {
            xAxis: {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                color: 'red',
                data: [150, 230, 224, 218, 135, 147, 260],
                type: 'line'
            }]
        }
    }

    setChartInst(chartInst, compInst) {
        console.info("setChartInst", chartInst, compInst);
    }

}

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
        //
        this.chart1 = new MyChart1()
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

.getoptions1-test-164005da {


}

</style>
