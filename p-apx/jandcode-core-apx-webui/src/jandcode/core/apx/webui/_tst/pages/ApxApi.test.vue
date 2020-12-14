<template>
    <tst-apx-panel class="apxapi-test-c5b2ff8a">
        Смотрим консоль
    </tst-apx-panel>
</template>

<script>
import {apx, tst} from '../vendor'

console.info("apx", apx);

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

.apxapi-test-c5b2ff8a {


}

</style>
