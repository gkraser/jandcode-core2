<template>
    <tst-apx-panel class="tstpanels1-test-24420820">
        <template #tools>
            <tst-checkbox label="paneClass" v-model="cfg.paneClass"/>
        </template>

        <tst-panels>
            <jc-panel :class="paneClass">hello</jc-panel>
        </tst-panels>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({
            paneClass: true
        })
    },
    data() {
        return {
            paneClass: 'pane',
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
            this.paneClass = cfg.paneClass ? 'pane' : 'pane-1'
        },
    }
}
</script>

<style lang="less">

.tstpanels1-test-24420820 {

  .pane {
    background-color: #a5d6a7;
  }

  .pane-1 {
    background-color: #c13c75;
  }

}

</style>
