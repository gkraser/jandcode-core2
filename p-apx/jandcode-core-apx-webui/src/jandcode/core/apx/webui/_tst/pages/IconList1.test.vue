<template>
    <tst-apx-panel class="iconlist1-test-b83df47e">
        <template #tools>
        </template>

        <tst-icon-list/>

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

.iconlist1-test-b83df47e {


}

</style>
