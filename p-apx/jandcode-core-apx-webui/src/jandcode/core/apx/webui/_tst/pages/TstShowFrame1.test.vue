<template>
    <tst-apx-panel class="tstshowframe1-test-2a497b4e">
        <tst-show-frame
                frame="jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame1.vue"
        />
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

.tstshowframe1-test-2a497b4e {


}

</style>
