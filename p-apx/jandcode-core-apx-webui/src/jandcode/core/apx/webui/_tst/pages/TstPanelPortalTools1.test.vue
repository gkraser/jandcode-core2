<template>
    <tst-apx-panel class="tstpanelportaltools1-test-fa8bce46">
        <template #tools>
        </template>

        <portal to="tools">
            <tst-btn label="from portal" @click="test1"/>
        </portal>

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
        test1(){
            console.info("test1!");
        }
    }
}
</script>

<style lang="less">

.tstpanelportaltools1-test-fa8bce46 {


}

</style>
