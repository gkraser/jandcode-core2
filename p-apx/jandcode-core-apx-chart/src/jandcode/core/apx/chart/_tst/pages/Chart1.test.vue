<template>
    <tst-apx-panel class="chart1-test-36127fde">
        <template #tools>
        </template>

        hello chart

        <jc-chart></jc-chart>

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

.chart1-test-36127fde {


}

</style>
