<template>
    <tst-apx-panel class="builder1-test-a86f99b9">
        <tst-panels>
            <jc-chart :options="chart1"></jc-chart>
        </tst-panels>
    </tst-apx-panel>
</template>

<script>
import {tst, mod, test} from '../vendor'

function dataset1() {
    let data = []
    let ut = new test.RndUtils()

    let idx = 0
    for (let dt of ut.days(30)) {
        let rec = {
            dt: dt,
            p1: ut.rnd.integer({min: 100, max: 200}),
            p2: ut.rnd.integer({min: 200, max: 300}),
        }
        data.push(rec)
        idx++
    }

    return data
}

/**
 * params.dataset - данные
 */
class Chart1 extends mod.ChartBuilder {

    onBuild() {
        super.onBuild();
        //
        let opt = this.getOptions()
        //
        opt.animation = false
        //
        this.add('dataset', {
            id: 'main',
            source: this.params.dataset
        })
        //
        this.addGrid({id: 'main'}, {
            x: {type: 'time'},
            y: {}
        })
        //
        this.add('series', {
            type: 'line',
            encode: {
                x: 'dt',
                y: 'p1'
            }
        })
        //
        this.add('series', {
            type: 'bar',
            encode: {
                x: 'dt',
                y: 'p2'
            }
        })
    }

}

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
        this.chart1 = new Chart1({
            dataset: dataset1()
        })
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

.builder1-test-a86f99b9 {


}

</style>
