<template>
    <tst-apex-panel>
        <template #tools>
        </template>

        <jc-btn label="hello"/>
    </tst-apex-panel>
</template>

<script>
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
