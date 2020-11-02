<template>
    <tst-apex-panel>
        <tst-icon-list/>
    </tst-apex-panel>
</template>

<script>

import {main} from '../vendor'

// куда монтировать (по умолчанию в .jc-app--main)
// export let mountTo = '.jc-app--main'

main.run()

export default {
    extends: Vue.component('tst-apex-page'),
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
