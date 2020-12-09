<template>
    <tst-apx-panel>
        <tst-icon-list/>
    </tst-apx-panel>
</template>

<script>

import {main, tst} from '../vendor'

// куда монтировать (по умолчанию в .jc-app--main)
// export let mountTo = '.jc-app--main'

main.run()

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
</style>
