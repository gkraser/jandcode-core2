<template>
    <tst-apx-panel>
        <tst-icon-list/>
    </tst-apx-panel>
</template>

<script>

import {main} from '../vendor'

// куда монтировать (по умолчанию в .jc-app--main)
// export let mountTo = '.jc-app--main'

main.run()

export default {
    extends: Vue.component('tst-apx-page'),
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
